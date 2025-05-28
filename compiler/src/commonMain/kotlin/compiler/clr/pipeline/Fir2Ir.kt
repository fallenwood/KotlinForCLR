package compiler.clr.pipeline

import compiler.clr.CLRConfigurationKeys
import compiler.clr.backend.FirClrBuiltinProviderActualDeclarationExtractor
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.jvm.JvmIrDeserializerImpl
import org.jetbrains.kotlin.backend.jvm.JvmIrSpecialAnnotationSymbolProvider
import org.jetbrains.kotlin.backend.jvm.JvmIrTypeSystemContext
import org.jetbrains.kotlin.builtins.DefaultBuiltIns
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.pipeline.PipelinePhase
import org.jetbrains.kotlin.config.AnalysisFlags
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JvmAnalysisFlags
import org.jetbrains.kotlin.config.languageVersionSettings
import org.jetbrains.kotlin.diagnostics.impl.BaseDiagnosticsCollector
import org.jetbrains.kotlin.fir.backend.Fir2IrConfiguration
import org.jetbrains.kotlin.fir.backend.jvm.FirDirectJavaActualDeclarationExtractor
import org.jetbrains.kotlin.fir.backend.jvm.FirJvmVisibilityConverter
import org.jetbrains.kotlin.fir.backend.jvm.JvmFir2IrExtensions
import org.jetbrains.kotlin.fir.pipeline.Fir2IrActualizedResult
import org.jetbrains.kotlin.fir.pipeline.FirResult
import org.jetbrains.kotlin.fir.pipeline.convertToIrAndActualize
import org.jetbrains.kotlin.ir.backend.jvm.serialization.JvmIrMangler
import org.jetbrains.kotlin.ir.util.dump
import java.io.File

object Fir2Ir : PipelinePhase<ClrFrontendPipelineArtifact, ClrFir2IrPipelineArtifact>(
	name = "ClrFir2IrPipelinePhase"
) {
	override fun executePhase(input: ClrFrontendPipelineArtifact): ClrFir2IrPipelineArtifact? {
		val (firResult, configuration, environment, diagnosticCollector, sourceFiles) = input
		val irGenerationExtensions = IrGenerationExtension.Companion.getInstances(environment.project)

		val jvmFir2IrExtensions = JvmFir2IrExtensions(configuration, JvmIrDeserializerImpl())

		val fir2IrAndIrActualizerResult = firResult.convertToIrAndActualizeForClr(
			jvmFir2IrExtensions,
			configuration,
			diagnosticCollector,
			irGenerationExtensions
		)

		// 输出IR调试信息
		File(input.configuration.get(CLRConfigurationKeys.OUTPUT_DIRECTORY)!!, "Kotlin IR.txt").printWriter().use { writer ->
			writer.println(fir2IrAndIrActualizerResult.irModuleFragment.dump())
		}

		return ClrFir2IrPipelineArtifact(
			fir2IrAndIrActualizerResult,
			configuration,
			environment,
			diagnosticCollector,
			sourceFiles,
		)
	}

	fun FirResult.convertToIrAndActualizeForClr(
		fir2IrExtensions: JvmFir2IrExtensions,
		configuration: CompilerConfiguration,
		diagnosticsReporter: BaseDiagnosticsCollector,
		irGeneratorExtensions: Collection<IrGenerationExtension>,
	): Fir2IrActualizedResult {
		val performanceManager = configuration[CLIConfigurationKeys.PERF_MANAGER]
		performanceManager?.notifyIRTranslationStarted()

		val fir2IrConfiguration = Fir2IrConfiguration.forJvmCompilation(configuration, diagnosticsReporter)

		val result = convertToIrAndActualize(
			fir2IrExtensions,
			fir2IrConfiguration,
			irGeneratorExtensions,
			JvmIrMangler,
			FirJvmVisibilityConverter,
			DefaultBuiltIns.Instance,
			::JvmIrTypeSystemContext,
			JvmIrSpecialAnnotationSymbolProvider,
			if (configuration.languageVersionSettings.getFlag(AnalysisFlags.stdlibCompilation)
				&& configuration.languageVersionSettings.getFlag(JvmAnalysisFlags.expectBuiltinsAsPartOfStdlib)
			) {
				{ emptyList() }
			} else {
				{
					listOfNotNull(
						FirClrBuiltinProviderActualDeclarationExtractor.initializeIfNeeded(it),
						FirDirectJavaActualDeclarationExtractor.initializeIfNeeded(it)
					)
				}
			}
		)

		performanceManager?.notifyIRTranslationFinished()
		return result
	}
}
