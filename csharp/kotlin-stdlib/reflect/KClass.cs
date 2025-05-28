namespace kotlin.reflect;

public interface KClass : KClassifier {
	public string? simpleName { get; }
	public string? qualifiedName { get; }
	public bool isInstance(object? value);
}