package compiler.clr.pipeline

import compiler.clr.backend.GenerationState
import compiler.clr.frontend.VfsBasedProjectEnvironment
import org.jetbrains.kotlin.KtSourceFile
import org.jetbrains.kotlin.cli.pipeline.Fir2IrPipelineArtifact
import org.jetbrains.kotlin.cli.pipeline.FrontendPipelineArtifact
import org.jetbrains.kotlin.cli.pipeline.PipelineArtifact
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.diagnostics.impl.BaseDiagnosticsCollector
import org.jetbrains.kotlin.fir.pipeline.Fir2IrActualizedResult
import org.jetbrains.kotlin.fir.pipeline.FirResult

data class ClrFrontendPipelineArtifact(
	override val result: FirResult,
	val configuration: CompilerConfiguration,
	val environment: VfsBasedProjectEnvironment,
	val diagnosticCollector: BaseDiagnosticsCollector,
	val sourceFiles: List<KtSourceFile>,
) : FrontendPipelineArtifact()

data class ClrFir2IrPipelineArtifact(
	override val result: Fir2IrActualizedResult,
	val configuration: CompilerConfiguration,
	val environment: VfsBasedProjectEnvironment,
	val diagnosticCollector: BaseDiagnosticsCollector,
	val sourceFiles: List<KtSourceFile>,
) : Fir2IrPipelineArtifact()

class ClrBinaryPipelineArtifact(val outputs: List<GenerationState>) : PipelineArtifact()