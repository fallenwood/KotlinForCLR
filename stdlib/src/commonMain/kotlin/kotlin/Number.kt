package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
abstract class Number {
	abstract fun toDouble(): Double
	abstract fun toFloat(): Float
	abstract fun toLong(): Long
	abstract fun toInt(): Int
	open fun toChar(): Char {
		return toInt().toChar()
	}
	abstract fun toShort(): Short
	abstract fun toByte(): Byte
}