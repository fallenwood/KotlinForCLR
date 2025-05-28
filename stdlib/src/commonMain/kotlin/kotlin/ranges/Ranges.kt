package kotlin.ranges

private open class ComparableRange<T : Comparable<T>>(
	override val start: T,
	override val endInclusive: T
) : ClosedRange<T> {
	override fun equals(other: Any?): Boolean {
		return other is ComparableRange<*> && (isEmpty() && other.isEmpty() ||
				start == other.start && endInclusive == other.endInclusive)
	}

	override fun hashCode(): Int {
		return if (isEmpty()) -1 else 31 * start.hashCode() + endInclusive.hashCode()
	}

	override fun toString(): String = "$start..$endInclusive"
}

operator fun <T : Comparable<T>> T.rangeTo(that: T): ClosedRange<T> = ComparableRange(this, that)

private open class ComparableOpenEndRange<T : Comparable<T>>(
	override val start: T,
	override val endExclusive: T
) : OpenEndRange<T> {

	override fun equals(other: Any?): Boolean {
		return other is ComparableOpenEndRange<*> && (isEmpty() && other.isEmpty() ||
				start == other.start && endExclusive == other.endExclusive)
	}

	override fun hashCode(): Int {
		return if (isEmpty()) -1 else 31 * start.hashCode() + endExclusive.hashCode()
	}

	override fun toString(): String = "$start..<$endExclusive"
}

operator fun <T : Comparable<T>> T.rangeUntil(that: T): OpenEndRange<T> = ComparableOpenEndRange(this, that)

interface ClosedFloatingPointRange<T : Comparable<T>> : ClosedRange<T> {
	override fun contains(value: T): Boolean = lessThanOrEquals(start, value) && lessThanOrEquals(value, endInclusive)
	override fun isEmpty(): Boolean = !lessThanOrEquals(start, endInclusive)

	/**
	 * Compares two values of range domain type and returns true if first is less than or equal to second.
	 */
	fun lessThanOrEquals(a: T, b: T): Boolean
}

private class ClosedDoubleRange(
	start: Double,
	endInclusive: Double
) : ClosedFloatingPointRange<Double> {
	private val _start = start
	private val _endInclusive = endInclusive
	override val start: Double get() = _start
	override val endInclusive: Double get() = _endInclusive

	override fun lessThanOrEquals(a: Double, b: Double): Boolean = a <= b

	override fun contains(value: Double): Boolean = value >= _start && value <= _endInclusive
	override fun isEmpty(): Boolean = !(_start <= _endInclusive)

	override fun equals(other: Any?): Boolean {
		return other is ClosedDoubleRange && (isEmpty() && other.isEmpty() ||
				_start == other._start && _endInclusive == other._endInclusive)
	}

	override fun hashCode(): Int {
		return if (isEmpty()) -1 else 31 * _start.hashCode() + _endInclusive.hashCode()
	}

	override fun toString(): String = "$_start..$_endInclusive"
}

operator fun Double.rangeTo(that: Double): ClosedFloatingPointRange<Double> = ClosedDoubleRange(this, that)

private class OpenEndDoubleRange(
	start: Double,
	endExclusive: Double
) : OpenEndRange<Double> {
	private val _start = start
	private val _endExclusive = endExclusive
	override val start: Double get() = _start
	override val endExclusive: Double get() = _endExclusive

	private fun lessThanOrEquals(a: Double, b: Double): Boolean = a <= b

	override fun contains(value: Double): Boolean = value >= _start && value < _endExclusive
	override fun isEmpty(): Boolean = !(_start < _endExclusive)

	override fun equals(other: Any?): Boolean {
		return other is OpenEndDoubleRange && (isEmpty() && other.isEmpty() ||
				_start == other._start && _endExclusive == other._endExclusive)
	}

	override fun hashCode(): Int {
		return if (isEmpty()) -1 else 31 * _start.hashCode() + _endExclusive.hashCode()
	}

	override fun toString(): String = "$_start..<$_endExclusive"
}

operator fun Double.rangeUntil(that: Double): OpenEndRange<Double> = OpenEndDoubleRange(this, that)

private class ClosedFloatRange(
	start: Float,
	endInclusive: Float
) : ClosedFloatingPointRange<Float> {
	private val _start = start
	private val _endInclusive = endInclusive
	override val start: Float get() = _start
	override val endInclusive: Float get() = _endInclusive

	override fun lessThanOrEquals(a: Float, b: Float): Boolean = a <= b

	override fun contains(value: Float): Boolean = value >= _start && value <= _endInclusive
	override fun isEmpty(): Boolean = !(_start <= _endInclusive)

	override fun equals(other: Any?): Boolean {
		return other is ClosedFloatRange && (isEmpty() && other.isEmpty() ||
				_start == other._start && _endInclusive == other._endInclusive)
	}

	override fun hashCode(): Int {
		return if (isEmpty()) -1 else 31 * _start.hashCode() + _endInclusive.hashCode()
	}

	override fun toString(): String = "$_start..$_endInclusive"
}

operator fun Float.rangeTo(that: Float): ClosedFloatingPointRange<Float> = ClosedFloatRange(this, that)

private class OpenEndFloatRange(
	start: Float,
	endExclusive: Float
) : OpenEndRange<Float> {
	private val _start = start
	private val _endExclusive = endExclusive
	override val start: Float get() = _start
	override val endExclusive: Float get() = _endExclusive

	private fun lessThanOrEquals(a: Float, b: Float): Boolean = a <= b

	override fun contains(value: Float): Boolean = value >= _start && value < _endExclusive
	override fun isEmpty(): Boolean = !(_start < _endExclusive)

	override fun equals(other: Any?): Boolean {
		return other is OpenEndFloatRange && (isEmpty() && other.isEmpty() ||
				_start == other._start && _endExclusive == other._endExclusive)
	}

	override fun hashCode(): Int {
		return if (isEmpty()) -1 else 31 * _start.hashCode() + _endExclusive.hashCode()
	}

	override fun toString(): String = "$_start..<$_endExclusive"
}

operator fun Float.rangeUntil(that: Float): OpenEndRange<Float> = OpenEndFloatRange(this, that)

inline operator fun <T, R> R.contains(element: T?): Boolean where T : Any, R : ClosedRange<T>, R : Iterable<T> =
	element != null && contains(element)

inline operator fun <T, R> R.contains(element: T?): Boolean where T : Any, R : OpenEndRange<T>, R : Iterable<T> =
	element != null && contains(element)

internal fun checkStepIsPositive(isPositive: Boolean, step: Number) {
	if (!isPositive) throw IllegalArgumentException("Step must be positive, was: $step.")
}