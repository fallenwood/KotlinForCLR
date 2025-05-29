package compiler.clr

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

object AssemblyResolver {
	fun resolve(assembly: String) = resolveAssembly(
		File("..\\csharp\\AssemblyResolver\\bin\\Release\\net9.0\\KotlinCLRBackendCompiler.dll").absolutePath,
		assembly
	)
}

fun resolveAssembly(programPath: String, assembly: String): NodeAssembly {
	val process = ProcessBuilder("dotnet", "\"$programPath\"", "\"$assembly\"")
		.start()
	val json = process.inputReader(Charsets.UTF_8).use {
		it.readText()
	}
	return Json.decodeFromString<NodeAssembly>(json)
}

abstract class AssemblyNode

@Serializable
data class NodeAssembly(
	val name: String?,
	val types: List<NodeType>,
) : AssemblyNode()

@Serializable
data class NodeType(
	val name: String,
	val namespace: String?,
	val baseType: String?,
	val interfaces: List<String?>,
	val attributes: List<String?>,
	val constructors: List<NodeConstructor>,
	val events: List<NodeEvent>,
	val fields: List<NodeField>,
	val methods: List<NodeMethod>,
	val nestedTypes: List<NodeType>,
	val properties: List<NodeProperty>,
	val isAbstract: Boolean,
	val isArray: Boolean,
	val isClass: Boolean,
	val isEnum: Boolean,
	val isGenericParameter: Boolean,
	val isGenericType: Boolean,
	val isGenericTypeDefinition: Boolean,
	val isInterface: Boolean,
	val isNested: Boolean,
	val isNestedAssembly: Boolean,
	val isNestedFamily: Boolean,
	val isNestedPrivate: Boolean,
	val isNestedPublic: Boolean,
	val isNotPublic: Boolean,
	val isPointer: Boolean,
	val isPrimitive: Boolean,
	val isPublic: Boolean,
	val isSealed: Boolean,
	val isSignatureType: Boolean,
	val isTypeDefinition: Boolean,
	val isValueType: Boolean,
	val isVisible: Boolean,
) : AssemblyNode()

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
	val returnType: String?,
	val genericArguments: List<String?>,
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
	val type: String?,
	val position: Int,
) : AssemblyNode()