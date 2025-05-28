namespace kotlin;

public class Deprecated(
	string message,
	ReplaceWith? replaceWith = null,
	DeprecationLevel level = DeprecationLevel.WARNING
) : Attribute {
	public string message { get; } = message;
	public ReplaceWith replaceWith { get; } = replaceWith ?? new ReplaceWith("");
	public DeprecationLevel level { get; } = level;
}

public class DeprecatedSinceKotlin(
	string warningSince = "",
	string errorSince = "",
	string hiddenSince = ""
) : Attribute {
	public string warningSince { get; } = warningSince;
	public string errorSince { get; } = errorSince;
	public string hiddenSince { get; } = hiddenSince;
}

public class ReplaceWith(
	string expression,
	params string[] imports
) : Attribute {
	public string expression { get; } = expression;
	public string[] imports { get; } = imports;
}

public enum DeprecationLevel {
	WARNING,
	ERROR,
	HIDDEN
}

public class ExtensionFunctionType : Attribute;

public class ContextFunctionTypeParams(int count) : Attribute {
	public int count { get; } = count;
}

public class ParameterName(string name) : Attribute {
	public string name { get; } = name;
}

public class Suppress(params string[] names) : Attribute {
	public string[] names { get; } = names;
}
public class UnsafeVariance : Attribute;

public class SinceKotlin(string version) : Attribute {
	public string version { get; } = version;
}

public class DslMarker : Attribute;

public class PublishedApi : Attribute;