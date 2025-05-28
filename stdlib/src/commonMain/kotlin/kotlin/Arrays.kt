package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
class ByteArray(val size: Int) {
	@Suppress("WRONG_MODIFIER_TARGET")
	inline constructor(size: Int, init: (Int) -> Byte) : this(size)
	external operator fun get(index: Int): Byte
	external operator fun set(index: Int, value: Byte)
	external operator fun iterator(): ByteIterator
}

@ActualizeByClrBuiltinProvider
class CharArray(val size: Int) {
	@Suppress("WRONG_MODIFIER_TARGET")
	inline constructor(size: Int, init: (Int) -> Char) : this(size)
	external operator fun get(index: Int): Char
	external operator fun set(index: Int, value: Char)
	external operator fun iterator(): CharIterator
}

@ActualizeByClrBuiltinProvider
class ShortArray(val size: Int) {
	@Suppress("WRONG_MODIFIER_TARGET")
	inline constructor(size: Int, init: (Int) -> Short) : this(size)
	external operator fun get(index: Int): Short
	external operator fun set(index: Int, value: Short)
	external operator fun iterator(): ShortIterator
}

@ActualizeByClrBuiltinProvider
class IntArray(val size: Int) {
	@Suppress("WRONG_MODIFIER_TARGET")
	inline constructor(size: Int, init: (Int) -> Int) : this(size)
	external operator fun get(index: Int): Int
	external operator fun set(index: Int, value: Int)
	external operator fun iterator(): IntIterator
}

@ActualizeByClrBuiltinProvider
class LongArray(val size: Int) {
	@Suppress("WRONG_MODIFIER_TARGET")
	inline constructor(size: Int, init: (Int) -> Long) : this(size)
	external operator fun get(index: Int): Long
	external operator fun set(index: Int, value: Long)
	external operator fun iterator(): LongIterator
}

@ActualizeByClrBuiltinProvider
class FloatArray(val size: Int) {
	@Suppress("WRONG_MODIFIER_TARGET")
	inline constructor(size: Int, init: (Int) -> Float) : this(size)
	external operator fun get(index: Int): Float
	external operator fun set(index: Int, value: Float)
	external operator fun iterator(): FloatIterator
}

@ActualizeByClrBuiltinProvider
class DoubleArray(val size: Int) {
	@Suppress("WRONG_MODIFIER_TARGET")
	inline constructor(size: Int, init: (Int) -> Double) : this(size)
	external operator fun get(index: Int): Double
	external operator fun set(index: Int, value: Double)
	external operator fun iterator(): DoubleIterator
}

@ActualizeByClrBuiltinProvider
class BooleanArray(val size: Int) {
	@Suppress("WRONG_MODIFIER_TARGET")
	inline constructor(size: Int, init: (Int) -> Boolean) : this(size)
	external operator fun get(index: Int): Boolean
	external operator fun set(index: Int, value: Boolean)
	external operator fun iterator(): BooleanIterator
}