/*
   Copyright 2025 Nyayurin

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

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