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