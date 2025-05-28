package kotlin.ranges

interface ClosedRange<T : Comparable<T>> {
	val start: T
	val endInclusive: T
	operator fun contains(value: T): Boolean = value >= start && value <= endInclusive
	fun isEmpty(): Boolean = start > endInclusive
}

interface OpenEndRange<T : Comparable<T>> {
	val start: T
	val endExclusive: T
	operator fun contains(value: T): Boolean = value >= start && value < endExclusive
	fun isEmpty(): Boolean = start >= endExclusive
}