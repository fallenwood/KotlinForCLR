package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
class Char private constructor() : Comparable<Char> {
	external override fun compareTo(other: Char): Int
	external operator fun plus(other: Int): Char
	external operator fun minus(other: Char): Int
	external operator fun minus(other: Int): Char
	external operator fun inc(): Char
	external operator fun dec(): Char
	external operator fun rangeTo(other: Char): CharRange
	external operator fun rangeUntil(other: Char): CharRange
	external fun toByte(): Byte
	external fun toChar(): Char
	external fun toShort(): Short
	external fun toInt(): Int
	external fun toLong(): Long
	external fun toFloat(): Float
	external fun toDouble(): Double
	external override fun toString(): String
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	companion object {
		const val MIN_VALUE: Char = '\u0000'
		const val MAX_VALUE: Char = '\uFFFF'
		const val MIN_HIGH_SURROGATE: Char = '\uD800'
		const val MAX_HIGH_SURROGATE: Char = '\uDBFF'
		const val MIN_LOW_SURROGATE: Char = '\uDC00'
		const val MAX_LOW_SURROGATE: Char = '\uDFFF'
		const val MIN_SURROGATE: Char = MIN_HIGH_SURROGATE
		const val MAX_SURROGATE: Char = MAX_LOW_SURROGATE
		const val SIZE_BYTES: Int = 2
		const val SIZE_BITS: Int = 16
	}
}