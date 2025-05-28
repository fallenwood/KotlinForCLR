package compiler.clr.pipeline

import compiler.clr.CLRCompiler
import compiler.clr.CLRCompilerArguments
import org.jetbrains.kotlin.backend.common.phaser.then
import org.jetbrains.kotlin.cli.pipeline.AbstractCliPipeline

class Pipeline(
	override val defaultPerformanceManager: CLRCompiler.PerformanceManager
) : AbstractCliPipeline<CLRCompilerArguments>() {
	override fun createCompoundPhase(
		arguments: CLRCompilerArguments
	) = Configuration then Frontend then Fir2Ir then Backend
}