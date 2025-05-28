namespace kotlin.reflect;

public class KTypeProjection(
	KVariance? variance,
	KType? type
) {
	public KVariance? variance { get; } = variance;
	public KType? type { get; } = type;
}