/*
   Copyright 2025 Nyayurin

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package compiler.clr.frontend

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

fun resolveAssembly(
	programPath: String,
	assemblies: List<String>,
	assembly: String,
	dotnetPath: String,
	cacheSystemPath: Path,
	cacheRootPath: Path,
	cacheLevel: CacheLevel): NodeAssembly {
	val filename = File(assembly).name + ".resolved"
	val isSystemPackage = assembly.startsWith(dotnetPath)
	val shouldCache = shouldCache(isSystemPackage, cacheLevel)

	val cacheFile = when (isSystemPackage) {
		false -> Paths.get(cacheRootPath.absolutePathString(), filename)
		true -> Paths.get(cacheSystemPath.absolutePathString(), filename)
	}.let { File (it.absolutePathString())}

	if (shouldCache && cacheFile.exists()) {
		println("use cached: $assembly")
		cacheFile.inputStream().use { stream ->
			val string = stream.readAllBytes().toString(Charsets.UTF_8)

			try {
				return Json.decodeFromString<NodeAssembly>(string)
			} catch (e: Exception) {
				println("exception: decoding \"${cacheFile.absolutePath}\"")
			}
		}
	}

	println("processing: $assembly")
	val process = ProcessBuilder("dotnet", "\"$programPath\"", "\"${assemblies.joinToString(";")}\" \"$assembly\"")
		.start()
	val json = process.inputReader(Charsets.UTF_8).use {
		it.readText()
	}
	val resolved = try {
		Json.decodeFromString<NodeAssembly>(json)
	} catch (e: Exception) {
		println("exception: dotnet \"$programPath\" \"${assemblies.joinToString(";")}\" \"$assembly\"")
		println(json)
		throw e
	}

	if (shouldCache) {
		cacheFile.outputStream().use { stream ->
			stream.writer(Charsets.UTF_8).use { writer ->
				writer.write(json)
			}
		}
	}

	return resolved
}

private fun shouldCache(isSystemPackage: Boolean, cacheLavel: CacheLevel): Boolean {
	return when (cacheLavel) {
		CacheLevel.None -> false
		CacheLevel.System -> isSystemPackage
		CacheLevel.All -> true
	}
}

enum class CacheLevel {
	None,
	System,
	All,
}

abstract class AssemblyNode

@Serializable
data class NodeAssembly(
	val name: String,
	val types: List<NodeType>,
) : AssemblyNode()

@Serializable
data class NodeType(
	val name: String,
	val namespace: String,
	val baseType: NodeTypeReference?,
	val interfaces: List<NodeTypeReference>,
	val attributes: List<NodeAttribute>,
	val constructors: List<NodeConstructor>,
	val events: List<NodeEvent>,
	val fields: List<NodeField>,
	val methods: List<NodeMethod>,
	val nestedTypes: List<NodeType>,
	val properties: List<NodeProperty>,
	val typeParameters: List<NodeTypeParameter>,
	val isAbstract: Boolean,
	val isArray: Boolean,
	val isClass: Boolean,
	val isEnum: Boolean,
	val isGenericType: Boolean,
	val isInterface: Boolean,
	val isNested: Boolean,
	val isNestedAssembly: Boolean,
	val isNestedFamily: Boolean,
	val isNestedPrivate: Boolean,
	val isNestedPublic: Boolean,
	val isNotPublic: Boolean,
	val isPointer: Boolean,
	val isPublic: Boolean,
	val isSealed: Boolean,
	val isValueType: Boolean,
) : AssemblyNode() {
	fun match(namespace: String, name: String) = this.namespace == namespace && this.name == name
	fun match(name: String) = this.namespace == "" && this.name == name
}

@Serializable
data class NodeConstructor(
	val parameters: List<NodeParameter>,
	val isAbstract: Boolean,
	val isAssembly: Boolean,
	val isFamily: Boolean,
	val isFinal: Boolean,
	val isPrivate: Boolean,
	val isPublic: Boolean,
	val isStatic: Boolean,
	val isVirtual: Boolean,
) : AssemblyNode()

@Serializable
data object NodeEvent : AssemblyNode()

@Serializable
data object NodeField : AssemblyNode()

@Serializable
data class NodeMethod(
	val name: String,
	val returnType: NodeTypeReference,
	val attributes: List<NodeAttribute>,
	val typeParameters: List<NodeTypeParameter>,
	val parameters: List<NodeParameter>,
	val isAbstract: Boolean,
	val isAssembly: Boolean,
	val isFamily: Boolean,
	val isFinal: Boolean,
	val isPrivate: Boolean,
	val isPublic: Boolean,
	val isStatic: Boolean,
	val isVirtual: Boolean,
) : AssemblyNode()

@Serializable
data object NodeProperty : AssemblyNode()

@Serializable
data class NodeParameter(
	val name: String?,
	val type: NodeTypeReference,
	val attributes: List<NodeAttribute>,
	val hasDefaultValue: Boolean,
	val isParams: Boolean,
) : AssemblyNode()

@Serializable
data class NodeTypeParameter(
	val name: String,
	val isOut: Boolean,
	val isIn: Boolean,
	val isInType: Boolean,
	val isInMethod: Boolean,
) : AssemblyNode()

@Serializable
data class NodeAttribute(
	val type: NodeTypeReference?,
) : AssemblyNode()

@Serializable
data class NodeTypeReference(
	val namespace: String?,
	val name: String,
	val typeKind: Int,
	val typeParameter: NodeTypeParameter?,
	val typeParameters: List<NodeTypeParameter>?,
) : AssemblyNode() {
	fun match(namespace: String, name: String) = this.namespace == namespace && this.name == name
	fun match(name: String) = this.namespace == null && this.name == name
}