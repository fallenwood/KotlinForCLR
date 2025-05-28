package compiler.clr.backend

import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.messageCollector
import org.jetbrains.kotlin.config.moduleName
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.DiagnosticReporterFactory

class GenerationState(
	val project: Project,
	val module: ModuleDescriptor,
	val configuration: CompilerConfiguration,
	val moduleName: String? = configuration.moduleName,
	val diagnosticReporter: DiagnosticReporter = DiagnosticReporterFactory.createReporter(configuration.messageCollector)
)