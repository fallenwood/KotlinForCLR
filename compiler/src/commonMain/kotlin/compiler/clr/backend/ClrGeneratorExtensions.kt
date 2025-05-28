package compiler.clr.backend

import org.jetbrains.kotlin.ir.expressions.IrConstructorCall

interface ClrGeneratorExtensions {
	fun generateRawTypeAnnotationCall(): IrConstructorCall?

	val cachedFields: CachedFieldsForObjectInstances
}