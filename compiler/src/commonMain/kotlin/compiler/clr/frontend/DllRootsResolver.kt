package compiler.clr.frontend

import compiler.clr.CSharpSourceRoot
import compiler.clr.ClrContentRootBase
import compiler.clr.ClrDllRootBase
import org.jetbrains.kotlin.cli.common.config.ContentRoot
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import org.jetbrains.kotlin.cli.jvm.index.JavaRoot
import org.jetbrains.kotlin.com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.kotlin.com.intellij.psi.PsiJavaModule
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.isValidJavaFqName
import org.jetbrains.kotlin.resolve.jvm.modules.JavaModule
import org.jetbrains.kotlin.resolve.jvm.modules.JavaModuleInfo

class DllRootsResolver(
	private val psiManager: PsiManager,
	private val messageCollector: MessageCollector?,
	private val contentRootToVirtualFile: (ClrContentRootBase) -> VirtualFile?,
	private val outputDirectory: VirtualFile?,
	hasKotlinSources: Boolean,
) {
	// Only report Java module-related errors if there's at least one Kotlin source file in the module. Otherwise the compiler would only
	// report those errors and not the more important "no source files" error which is handled later, after the roots have been computed.
	private val reportErrors = hasKotlinSources

	data class RootsAndModules(val roots: List<JavaRoot>, val modules: List<JavaModule>)

	private data class RootWithPrefix(val root: VirtualFile, val packagePrefix: String?)

	fun convertAssemblyRoots(contentRoots: List<ContentRoot>): RootsAndModules {
		val csharpSourceRoots = mutableListOf<RootWithPrefix>()
		val clrDllRoots = mutableListOf<VirtualFile>()

		for (contentRoot in contentRoots) {
			if (contentRoot !is ClrContentRootBase) continue
			val root = contentRootToVirtualFile(contentRoot) ?: continue
			when (contentRoot) {
				is CSharpSourceRoot -> csharpSourceRoots += RootWithPrefix(root, contentRoot.packagePrefix)
				is ClrDllRootBase -> clrDllRoots += root
				else -> error("Unknown root type: $contentRoot")
			}
		}

		return computeRoots(csharpSourceRoots, clrDllRoots)
	}

	private fun computeRoots(
		csharpSourceRoots: List<RootWithPrefix>,
		clrDllRoots: List<VirtualFile>,
	): RootsAndModules {
		val result = mutableListOf<JavaRoot>()
		val modules = mutableListOf<JavaModule>()

		val hasOutputDirectoryInClasspath = outputDirectory in clrDllRoots

		for ((root, packagePrefix) in csharpSourceRoots) {
			val modularRoot = modularSourceRoot(root, hasOutputDirectoryInClasspath)
			if (modularRoot != null) {
				modules += modularRoot
			} else {
				result += JavaRoot(root, JavaRoot.RootType.SOURCE, packagePrefix?.let { prefix ->
					if (isValidJavaFqName(prefix)) FqName(prefix)
					else null.also {
						report(
							CompilerMessageSeverity.STRONG_WARNING,
							"Invalid package prefix name is ignored: $prefix"
						)
					}
				})
			}
		}

		for (root in clrDllRoots) {
			result += JavaRoot(root, JavaRoot.RootType.BINARY)
		}

		//TODO: see also `addJvmSdkRoots` usages, some refactoring is required with moving such logic into one place
//		result += JavaRoot(javaModuleFinder.nonModuleRoot.file, JavaRoot.RootType.BINARY_SIG)

		return RootsAndModules(result, modules)
	}

	private fun findSourceModuleInfo(root: VirtualFile): Pair<VirtualFile, PsiJavaModule>? {
		val moduleInfoFile =
			when {
				root.isDirectory -> root.findChild(PsiJavaModule.MODULE_INFO_FILE)
				root.name == PsiJavaModule.MODULE_INFO_FILE -> root
				else -> null
			} ?: return null

		val psiFile = psiManager.findFile(moduleInfoFile) ?: return null
		val psiJavaModule = psiFile.children.singleOrNull { it is PsiJavaModule } as? PsiJavaModule ?: return null

		return moduleInfoFile to psiJavaModule
	}

	private fun modularSourceRoot(root: VirtualFile, hasOutputDirectoryInClasspath: Boolean): JavaModule.Explicit? {
		val (moduleInfoFile, psiJavaModule) = findSourceModuleInfo(root) ?: return null
		val sourceRoot = JavaModule.Root(root, isBinary = false)
		val roots =
			if (hasOutputDirectoryInClasspath)
				listOf(sourceRoot, JavaModule.Root(outputDirectory!!, isBinary = true))
			else listOf(sourceRoot)
		return JavaModule.Explicit(JavaModuleInfo.Companion.create(psiJavaModule), roots, moduleInfoFile)
	}

	private fun report(severity: CompilerMessageSeverity, message: String, file: VirtualFile? = null) {
		if (messageCollector == null) {
			throw IllegalStateException("${if (file != null) file.path + ":" else ""}$severity: $message (no MessageCollector configured)")
		}
		if (severity == CompilerMessageSeverity.ERROR && !reportErrors) return
		messageCollector.report(
			severity, message,
			if (file == null) null else CompilerMessageLocation.Companion.create(MessageUtil.virtualFileToPath(file))
		)
	}
}