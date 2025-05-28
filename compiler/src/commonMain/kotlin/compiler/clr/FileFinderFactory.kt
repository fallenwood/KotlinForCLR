package compiler.clr

import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.load.kotlin.MetadataFinderFactory
import org.jetbrains.kotlin.load.kotlin.VirtualFileFinder
import org.jetbrains.kotlin.load.kotlin.VirtualFileFinderFactory
import org.jetbrains.kotlin.serialization.deserialization.KotlinMetadataFinder

class ClrAssemblyFileFinderFactory(
	private val assemblies: Map<String, NodeAssembly>,
) : VirtualFileFinderFactory {
	// 确保即使没有显式加载System程序集也能处理
	private fun getEffectiveAssemblies(): Map<String, NodeAssembly> {
		return assemblies
	}
	
	override fun create(scope: GlobalSearchScope): VirtualFileFinder {
		return ClrAssemblyFileFinder(getEffectiveAssemblies(), scope)
	}

	override fun create(
		project: Project,
		module: ModuleDescriptor,
	): VirtualFileFinder {
		return ClrAssemblyFileFinder(getEffectiveAssemblies(), GlobalSearchScope.allScope(project))
	}
}

class ClrMetadataFinderFactory(
	private val fileFinderFactory: ClrAssemblyFileFinderFactory,
) : MetadataFinderFactory {
	override fun create(scope: GlobalSearchScope): KotlinMetadataFinder =
		fileFinderFactory.create(scope)

	override fun create(
		project: Project,
		module: ModuleDescriptor,
	): KotlinMetadataFinder =
		fileFinderFactory.create(project, module)
}