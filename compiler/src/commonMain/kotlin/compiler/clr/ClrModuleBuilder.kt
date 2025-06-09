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

package compiler.clr

import org.jetbrains.kotlin.modules.JavaRootPath
import org.jetbrains.kotlin.modules.Module
import java.util.ArrayList

interface ClrModule : Module {
	fun getAssemblyRoots(): List<String>
	fun getCSharpSourceRoots(): List<CSharpRootPath>
	override fun getClasspathRoots(): List<String> = error("Classpath entries are not supported in CLR module")
	override fun getJavaSourceRoots(): List<JavaRootPath> = error("Java source roots are not supported in CLR module")
}

class ClrModuleBuilder(
	private val name: String,
	private val outputDir: String,
	private val type: String,
) : ClrModule {
	private val sourceFiles = ArrayList<String>()
	private val commonSourceFiles = ArrayList<String>()
	private val assemblyRoots = ArrayList<String>()
	private val friendDirs = ArrayList<String>()
	private val csharpSourceRoots = ArrayList<CSharpRootPath>()
	override val modularJdkRoot: String? = null

	fun addSourceFiles(path: String) {
		sourceFiles.add(path)
	}

	fun addCommonSourceFiles(path: String) {
		commonSourceFiles.add(path)
	}

	fun addAssemblyEntry(path: String) {
		assemblyRoots.add(path)
	}

	fun addCSharpSourceRoot(rootPath: CSharpRootPath) {
		csharpSourceRoots.add(rootPath)
	}

	fun addFriendDir(friendDir: String) {
		friendDirs.add(friendDir)
	}

	override fun getAssemblyRoots(): List<String> = assemblyRoots
	override fun getCSharpSourceRoots(): List<CSharpRootPath> = csharpSourceRoots
	override fun getOutputDirectory(): String = outputDir
	override fun getFriendPaths(): List<String> = friendDirs
	override fun getSourceFiles(): List<String> = sourceFiles
	override fun getCommonSourceFiles(): List<String> = commonSourceFiles
	override fun getModuleName(): String = name
	override fun getModuleType(): String = type
}

data class CSharpRootPath(val path: String, val packagePrefix: String? = null)