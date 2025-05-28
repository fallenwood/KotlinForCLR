package compiler

import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.CommonCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.config.Services
import java.io.PrintStream

abstract class Compiler<A : CommonCompilerArguments> {
	abstract fun doExecute(
		arguments: A,
		services: Services,
		collector: MessageCollector
	): ExitCode

	fun exec(
		errStream: PrintStream,
		arguments: A,
		messageRenderer: MessageRenderer = defaultMessageRenderer()
	): ExitCode {
		val collector = PrintingMessageCollector(errStream, messageRenderer, arguments.verbose)
		return doExecute(arguments, Services.Companion.EMPTY, collector)
	}

	companion object {
		protected fun defaultMessageRenderer(): MessageRenderer =
			when (System.getProperty(MessageRenderer.PROPERTY_KEY)) {
				MessageRenderer.XML.name -> MessageRenderer.XML
				MessageRenderer.GRADLE_STYLE.name -> MessageRenderer.GRADLE_STYLE
				MessageRenderer.XCODE_STYLE.name -> MessageRenderer.XCODE_STYLE
				MessageRenderer.WITHOUT_PATHS.name -> MessageRenderer.WITHOUT_PATHS
				MessageRenderer.PLAIN_FULL_PATHS.name -> MessageRenderer.PLAIN_FULL_PATHS
				else -> MessageRenderer.PLAIN_RELATIVE_PATHS
			}
	}
}