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