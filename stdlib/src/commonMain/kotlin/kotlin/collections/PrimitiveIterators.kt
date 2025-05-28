package kotlin.collections

abstract class ByteIterator : Iterator<Byte> {
	final override fun next(): Byte = nextByte()
	abstract fun nextByte(): Byte
}

abstract class CharIterator : Iterator<Char> {
	final override fun next(): Char = nextChar()
	abstract fun nextChar(): Char
}

abstract class ShortIterator : Iterator<Short> {
	final override fun next(): Short = nextShort()
	abstract fun nextShort(): Short
}

abstract class IntIterator : Iterator<Int> {
	final override fun next(): Int = nextInt()
	abstract fun nextInt(): Int
}

abstract class LongIterator : Iterator<Long> {
	final override fun next(): Long = nextLong()
	abstract fun nextLong(): Long
}

abstract class FloatIterator : Iterator<Float> {
	final override fun next(): Float = nextFloat()
	abstract fun nextFloat(): Float
}

abstract class DoubleIterator : Iterator<Double> {
	final override fun next(): Double = nextDouble()
	abstract fun nextDouble(): Double
}

abstract class BooleanIterator : Iterator<Boolean> {
	final override fun next(): Boolean = nextBoolean()
	abstract fun nextBoolean(): Boolean
}