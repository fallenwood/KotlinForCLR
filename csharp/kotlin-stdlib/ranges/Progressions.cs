namespace kotlin.ranges;

public class CharProgression {
	internal CharProgression(
		char first,
		char endInclusive,
		int step
	) {
		this.first = first;
		this.step = step;
	}
	
	public char first { get; }
	public int step { get; }
}

public class IntProgression {
	internal IntProgression(
		int first,
		int endInclusive,
		int step
	) {
		this.first = first;
		this.step = step;
	}
	
	public int first { get; }
	public int step { get; }
}

public class LongProgression {
	internal LongProgression(
		long first,
		long endInclusive,
		long step
	) {
		this.first = first;
		this.step = step;
	}
	
	public long first { get; }
	public long step { get; }
}