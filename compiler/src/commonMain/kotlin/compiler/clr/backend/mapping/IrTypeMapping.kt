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
