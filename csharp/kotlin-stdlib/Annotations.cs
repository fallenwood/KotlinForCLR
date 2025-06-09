/*
   Copyright 2025 Nyayurin

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

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