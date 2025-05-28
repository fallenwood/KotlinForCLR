package compiler.clr.backend.mapping

import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrTypeArgument
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.ir.util.render

internal class PossiblyInnerIrType(
	val classifier: IrClass,
	val arguments: List<IrTypeArgument>,
	private val outerType: PossiblyInnerIrType?
) {
	fun segments(): List<PossiblyInnerIrType> = outerType?.segments().orEmpty() + this
}

private fun IrSimpleType.buildPossiblyInnerType(classifier: IrClass?, index: Int): PossiblyInnerIrType? {
	if (classifier == null) return null

	val toIndex = classifier.typeParameters.size + index
	if (!classifier.isInner) {
		assert(toIndex == arguments.size || classifier.visibility == DescriptorVisibilities.LOCAL) {
			"${arguments.size - toIndex} trailing arguments were found in this type: ${render()}"
		}

		return PossiblyInnerIrType(classifier, arguments.subList(index, toIndex), null)
	}

	val argumentsSubList = arguments.subList(index, toIndex)
	return PossiblyInnerIrType(
		classifier, argumentsSubList,
		buildPossiblyInnerType(classifier.parentAsClass, toIndex)
	)
}
