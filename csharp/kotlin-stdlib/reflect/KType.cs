namespace kotlin.reflect;

public interface KType {
	public KClassifier? classifier { get; }
	public List<KTypeProjection> arguments { get; }
	public bool isMarkedNullable { get; }
}