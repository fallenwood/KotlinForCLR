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

package compiler.clr.backend.lower

import compiler.clr.backend.ClrBackendContext
import org.jetbrains.kotlin.backend.common.lower.ExpectDeclarationRemover
import org.jetbrains.kotlin.backend.common.phaser.PhaseDescription
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.isExpect

@PhaseDescription(name = "ExpectDeclarationsRemoving")
internal class ClrExpectDeclarationRemover(private val context: ClrBackendContext) : ExpectDeclarationRemover(context.symbolTable, true) {
	@OptIn(UnsafeDuringIrConstructionAPI::class)
	override fun lower(irFile: IrFile) {
		irFile.declarations.removeIf { it.isExpect }
	}
}