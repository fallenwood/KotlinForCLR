package kotlin.ranges

internal class CharProgressionIterator(first: Char, last: Char, val step: Int) : CharIterator() {
	private val finalElement: Int = last.code
	private var hasNext: Boolean = if (step > 0) first <= last else first >= last
	private var next: Int = if (hasNext) first.code else finalElement

	override fun hasNext(): Boolean = hasNext
	external override fun nextChar(): Char
}

internal class IntProgressionIterator(first: Int, last: Int, val step: Int) : IntIterator() {
	private val finalElement: Int = last
	private var hasNext: Boolean = if (step > 0) first <= last else first >= last
	private var next: Int = if (hasNext) first else finalElement

	override fun hasNext(): Boolean = hasNext
	external override fun nextInt(): Int
}

internal class LongProgressionIterator(first: Long, last: Long, val step: Long) : LongIterator() {
	private val finalElement: Long = last
	private var hasNext: Boolean = if (step > 0) first <= last else first >= last
	private var next: Long = if (hasNext) first else finalElement

	override fun hasNext(): Boolean = hasNext
	external override fun nextLong(): Long
}