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