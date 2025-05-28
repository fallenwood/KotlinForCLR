namespace kotlin.reflect;

public interface KTypeParameter : KClassifier {
	public string name { get; }
	public List<KType> upperBounds { get; }
	public KVariance variance { get; }
	public bool isReified { get; }
}