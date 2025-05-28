package kotlin.ranges

open class CharProgression internal constructor(
	val first: Char,
	endInclusive: Char,
	val step: Int
) : Iterable<Char> {
	init {
		if (step == 0) throw IllegalArgumentException("Step must be non-zero.")
		if (step == Int.MIN_VALUE) throw IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.")
	}

	val last: Char
		external get

	external override fun iterator(): CharIterator
	open external fun isEmpty(): Boolean
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	external override fun toString(): String
	companion object {
		fun fromClosedRange(rangeStart: Char, rangeEnd: Char, step: Int): CharProgression = CharProgression(rangeStart, rangeEnd, step)
	}
}

open class IntProgression internal constructor(
	val first: Int,
	endInclusive: Int,
	val step: Int
) : Iterable<Int> {
	init {
		if (step == 0) throw IllegalArgumentException("Step must be non-zero.")
		if (step == Int.MIN_VALUE) throw IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.")
	}

	val last: Int
		external get

	external override fun iterator(): IntIterator
	open external fun isEmpty(): Boolean
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	external override fun toString(): String
	companion object {
		fun fromClosedRange(rangeStart: Int, rangeEnd: Int, step: Int): IntProgression = IntProgression(rangeStart, rangeEnd, step)
	}
}

open class LongProgression internal constructor(
	val first: Long,
	endInclusive: Long,
	val step: Long
) : Iterable<Long> {
	init {
		if (step == 0L) throw IllegalArgumentException("Step must be non-zero.")
		if (step == Long.MIN_VALUE) throw IllegalArgumentException("Step must be greater than Long.MIN_VALUE to avoid overflow on negation.")
	}

	val last: Long
		external get

	external override fun iterator(): LongIterator
	open external fun isEmpty(): Boolean
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	external override fun toString(): String
	companion object {
		fun fromClosedRange(rangeStart: Long, rangeEnd: Long, step: Long): LongProgression = LongProgression(rangeStart, rangeEnd, step)
	}
}