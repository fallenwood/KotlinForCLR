package compiler.clr

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.config.messageCollector
import org.jetbrains.kotlin.modules.Module
import java.io.File

fun CompilerConfiguration.report(severity: CompilerMessageSeverity, message: String) {
	messageCollector.report(severity, message)
}

fun CompilerConfiguration.applyModuleProperties(module: Module, buildFile: File?) {
	if (buildFile == null) return

	fun checkKeyIsNull(key: CompilerConfigurationKey<*>, name: String) {
		assert(get(key) == null) { "$name should be null, when buildFile is used" }
	}

	checkKeyIsNull(JVMConfigurationKeys.OUTPUT_DIRECTORY, "OUTPUT_DIRECTORY")
	checkKeyIsNull(JVMConfigurationKeys.OUTPUT_JAR, "OUTPUT_JAR")
	put(JVMConfigurationKeys.OUTPUT_DIRECTORY, File(module.getOutputDirectory()))
}