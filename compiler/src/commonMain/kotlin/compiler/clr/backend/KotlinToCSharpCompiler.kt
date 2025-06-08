package compiler.clr.backend

import compiler.clr.CLRConfigurationKeys
import compiler.clr.backend.codegen.render
import compiler.clr.backend.codegen.visit
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.fir.FirDiagnosticsCompilerResultsReporter
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.moduleName
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.diagnostics.impl.BaseDiagnosticsCollector
import org.jetbrains.kotlin.fir.pipeline.Fir2IrActualizedResult
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.modules.Module
import java.io.File
import java.io.FileWriter

object KotlinToCSharpCompiler {
	internal fun Fir2IrActualizedResult.toBackendInput(
		configuration: CompilerConfiguration
	): CSharpCodegenFactory.BackendInput {
		return CSharpCodegenFactory.BackendInput(
			irModuleFragment,
			irBuiltIns,
			symbolTable,
			components.irProviders,
			ClrGeneratorExtensionsImpl(configuration)
		)
	}

	internal fun runLowerings(
		project: Project,
		configuration: CompilerConfiguration,
		moduleDescriptor: ModuleDescriptor,
		module: Module?,
		codegenFactory: CSharpCodegenFactory,
		backendInput: CSharpCodegenFactory.BackendInput,
		diagnosticsReporter: BaseDiagnosticsCollector
	): CSharpCodegenFactory.CodegenInput {
		val state = GenerationState(
			project,
			moduleDescriptor,
			configuration,
			moduleName = module?.getModuleName() ?: configuration.moduleName,
			diagnosticReporter = diagnosticsReporter,
		)

		return codegenFactory.invokeLowerings(state, backendInput)
	}

	internal fun runCodegen(
		codegenInput: CSharpCodegenFactory.CodegenInput,
		codegenFactory: CSharpCodegenFactory,
		diagnosticsReporter: BaseDiagnosticsCollector,
		configuration: CompilerConfiguration
	) {
		val map = codegenFactory.invokeCodegen(codegenInput)
		File(configuration.get(CLRConfigurationKeys.OUTPUT_DIRECTORY)!!, "Code Node.xml").printWriter().use { writer ->
			map?.values?.forEach {
				writer.println(it.render())
			}
		}
		FirDiagnosticsCompilerResultsReporter.reportToMessageCollector(
			diagnosticsReporter,
			configuration.getNotNull(CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY),
			configuration.getBoolean(CLIConfigurationKeys.RENDER_DIAGNOSTIC_INTERNAL_NAME)
		)
		val destination = configuration.get(CLRConfigurationKeys.OUTPUT_DIRECTORY)!!
		map?.let { map ->
			for ((irFile, node) in map) {
				FileWriter(File(destination, irFile.name + ".cs")).use {
					it.write(node.visit())
				}
			}
		}
	}
}