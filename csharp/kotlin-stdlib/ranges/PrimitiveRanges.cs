namespace kotlin.ranges;

public sealed class CharRange(
	char start,
	char endInclusive
) : CharProgression(start, endInclusive, 1), ClosedRange, OpenEndRange;

public sealed class IntRange(
	int start,
	int endInclusive
) : IntProgression(start, endInclusive, 1), ClosedRange, OpenEndRange;

public sealed class LongRange(
	long start,
	long endInclusive
) : LongProgression(start, endInclusive, 1), ClosedRange, OpenEndRange;