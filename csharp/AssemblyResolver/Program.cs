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

using System.Reflection;
using System.Text.Json;

namespace KotlinCLRBackendCompiler;

public static class Program {
	public static void Main(string[] args) {
		Console.WriteLine(
			JsonSerializer.Serialize(
				NodeAssembly.from(Assembly.LoadFrom(args[0]))
			)
		);
	}
}

public record NodeAssembly(
	string? name,
	List<NodeType> types
) {
	public static NodeAssembly from(Assembly asm) => new(
		name: asm.GetName().Name,
		types: asm.GetTypes().Select(NodeType.from).ToList()
	);
}

public record NodeType(
	string name,
	string? @namespace,
	string? baseType,
	List<string?> interfaces,
	List<string?> attributes,
	List<NodeConstructor> constructors,
	List<NodeEvent> events,
	List<NodeField> fields,
	List<NodeMethod> methods,
	List<NodeType> nestedTypes,
	List<NodeProperty> properties,
	bool isAbstract,
	bool isArray,
	bool isClass,
	bool isEnum,
	bool isGenericParameter,
	bool isGenericType,
	bool isGenericTypeDefinition,
	bool isInterface,
	bool isNested,
	bool isNestedAssembly,
	bool isNestedFamily,
	bool isNestedPrivate,
	bool isNestedPublic,
	bool isNotPublic,
	bool isPointer,
	bool isPrimitive,
	bool isPublic,
	bool isSealed,
	bool isSignatureType,
	bool isTypeDefinition,
	bool isValueType,
	bool isVisible
) {
	public static NodeType from(Type type) => new(
		name: type.Name,
		@namespace: type.Namespace,
		baseType: type.BaseType?.FullName,
		interfaces: type.GetInterfaces().Select(it => it.FullName).ToList(),
		attributes: type.GetCustomAttributes().Select(it => it.GetType().FullName).ToList(),
		constructors: type.GetConstructors().Select(NodeConstructor.from).ToList(),
		events: type.GetEvents().Select(NodeEvent.from).ToList(),
		fields: type.GetFields().Select(NodeField.from).ToList(),
		methods: type.GetMethods().Select(NodeMethod.from).ToList(),
		nestedTypes: type.GetNestedTypes().Select(from).ToList(),
		properties: type.GetProperties().Select(NodeProperty.from).ToList(),
		isAbstract: type.IsAbstract,
		isArray: type.IsArray,
		isClass: type.IsClass,
		isEnum: type.IsEnum,
		isGenericParameter: type.IsGenericParameter,
		isGenericType: type.IsGenericType,
		isGenericTypeDefinition: type.IsGenericTypeDefinition,
		isInterface: type.IsInterface,
		isNested: type.IsNested,
		isNestedAssembly: type.IsNestedAssembly,
		isNestedFamily: type.IsNestedFamANDAssem,
		isNestedPrivate: type.IsNestedPrivate,
		isNestedPublic: type.IsNestedPublic,
		isNotPublic: type.IsNotPublic,
		isPointer: type.IsPointer,
		isPrimitive: type.IsPrimitive,
		isPublic: type.IsPublic,
		isSealed: type.IsSealed,
		isSignatureType: type.IsSignatureType,
		isTypeDefinition: type.IsTypeDefinition,
		isValueType: type.IsValueType,
		isVisible: type.IsVisible
	);
}

public record NodeConstructor(
	List<NodeParameter> parameters,
	bool isAbstract,
	bool isAssembly,
	bool isFamily,
	bool isFinal,
	bool isPrivate,
	bool isPublic,
	bool isStatic,
	bool isVirtual
) {
	public static NodeConstructor from(ConstructorInfo constructor) => new(
		parameters: constructor.GetParameters().Select(NodeParameter.from).ToList(),
		isAbstract: constructor.IsAbstract,
		isAssembly: constructor.IsAssembly,
		isFamily: constructor.IsFamily,
		isFinal: constructor.IsFinal,
		isPrivate: constructor.IsPrivate,
		isPublic: constructor.IsPublic,
		isStatic: constructor.IsStatic,
		isVirtual: constructor.IsVirtual
	);
}

public record NodeEvent(
) {
	public static NodeEvent from(EventInfo @event) => new(
	);
}

public record NodeField(
) {
	public static NodeField from(FieldInfo field) => new(
	);
}

public record NodeMethod(
	string name,
	string? returnType,
	List<string?> attributes,
	List<string?> genericArguments,
	List<NodeParameter> parameters,
	bool isAbstract,
	bool isAssembly,
	bool isFamily,
	bool isFinal,
	bool isPrivate,
	bool isPublic,
	bool isStatic,
	bool isVirtual
) {
	public static NodeMethod from(MethodInfo method) => new(
		name: method.Name,
		returnType: method.ReturnType.FullName,
		attributes: method.GetCustomAttributes().Select(it => it.GetType().FullName).ToList(),
		genericArguments: method.GetGenericArguments().Select(it => it.FullName).ToList(),
		parameters: method.GetParameters().Select(NodeParameter.from).ToList(),
		isAbstract: method.IsAbstract,
		isAssembly: method.IsAssembly,
		isFamily: method.IsFamily,
		isFinal: method.IsFinal,
		isPrivate: method.IsPrivate,
		isPublic: method.IsPublic,
		isStatic: method.IsStatic,
		isVirtual: method.IsVirtual
	);
}

public record NodeProperty(
) {
	public static NodeProperty from(PropertyInfo property) => new(
	);
}

public record NodeParameter(
	string? name,
	string? type,
	bool hasDefaultValue,
	int position
) {
	public static NodeParameter from(ParameterInfo parameter) => new(
		name: parameter.Name,
		type: parameter.ParameterType.FullName,
		hasDefaultValue: parameter.HasDefaultValue,
		position: parameter.Position
	);
}

public static class Extensions {
	public static T also<T>(this T value, Action<T> action) {
		action(value);
		return value;
	}

	public static R let<T, R>(this T value, Func<T, R> action) {
		return action(value);
	}
}