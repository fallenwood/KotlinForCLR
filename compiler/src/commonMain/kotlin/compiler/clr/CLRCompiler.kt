package compiler.clr

import compiler.Compiler
import compiler.clr.pipeline.Pipeline
import org.jetbrains.kotlin.cli.common.CommonCompilerPerformanceManager
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.config.Services

class CLRCompiler : Compiler<CLRCompilerArguments>() {
	private val defaultPerformanceManager = PerformanceManager()

	override fun doExecute(
		arguments: CLRCompilerArguments,
		services: Services,
		collector: MessageCollector,
	) = Pipeline(defaultPerformanceManager).execute(arguments, services, collector)

	class PerformanceManager : CommonCompilerPerformanceManager("Kotlin to CLR Compiler")
}