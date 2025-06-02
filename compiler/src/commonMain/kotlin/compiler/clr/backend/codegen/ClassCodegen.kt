package compiler.clr.backend.codegen

import compiler.clr.backend.ClrBackendContext
import compiler.clr.backend.mapping.IrTypeMapper
import org.jetbrains.kotlin.descriptors.ClassKind.*
import org.jetbrains.kotlin.descriptors.Modality.*
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.lazy.Fir2IrLazySimpleFunction
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol
import org.jetbrains.kotlin.ir.symbols.IrVariableSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.javac.resolve.classId

@OptIn(UnsafeDuringIrConstructionAPI::class)
class ClassCodegen(val context: ClrBackendContext) {
	val typeMapper = IrTypeMapper(context)

	fun IrFile.visit() = buildString {
		val `package` = packageFqName

		if (!`package`.isRoot) {
			append("namespace $`package`;")
			appendLine()
		}

		append(
			declarations.joinToString("\n") { declaration ->
				when (declaration) {
					is IrClass -> declaration.visit(0)
					else -> error(declaration)
				}
			}
		)
	}

	fun IrClass.visit(padding: Int) = when (kind) {
		CLASS -> visitClass(padding)
		INTERFACE -> visitInterface(padding)
		ENUM_CLASS -> visitEnumClass(padding)
		ENUM_ENTRY -> TODO()
		ANNOTATION_CLASS -> visitAnnotationClass(padding)
		OBJECT -> visitObject(padding)
	}

	fun IrClass.visitClass(padding: Int): String {
		return buildString {
			repeat(padding) { append("    ") }
			when (visibility.delegate) {
				is Visibilities.Private -> append("private ")
				is Visibilities.Protected -> append("protected ")
				is Visibilities.Internal -> append("internal ")
				is Visibilities.Public -> append("public ")
			}
			when (modality) {
				FINAL -> append("sealed ")
				ABSTRACT -> append("abstract ")
				SEALED -> TODO()
				OPEN -> {}
			}
			append("class ")
			append(name)
			append(" : ")
			append(superTypes.joinToString(", ") { typeMapper.mapType(it) })
			appendLine()
			repeat(padding) { append("    ") }
			append("{")
			appendLine()
			append(
				declarations
					.mapNotNull { it.visit(padding + 1) }
					.joinToString("\n")
			)
			appendLine()
			repeat(padding) { append("    ") }
			append("}")
		}
	}

	fun IrClass.visitInterface(padding: Int): String {
		return buildString {
			repeat(padding) { append("    ") }
			when (visibility.delegate) {
				is Visibilities.Private -> append("private ")
				is Visibilities.Protected -> append("protected ")
				is Visibilities.Internal -> append("internal ")
				is Visibilities.Public -> append("public ")
			}
			when (modality) {
				FINAL -> append("sealed ")
				ABSTRACT -> append("abstract ")
				SEALED -> TODO()
				OPEN -> {}
			}
			append("interface ")
			append(name)
			if (superTypes.isNotEmpty()) {
				append(" : ")
				append(superTypes.joinToString(", ") { typeMapper.mapType(it) })
			}
			appendLine()
			repeat(padding) { append("    ") }
			append("{")
			appendLine()
			append(
				declarations
					.mapNotNull { it.visit(padding + 1) }
					.joinToString("\n")
			)
			appendLine()
			repeat(padding) { append("    ") }
			append("}")
		}
	}

	fun IrClass.visitEnumClass(padding: Int): String {
		return buildString {
			repeat(padding) { append("    ") }
			when (visibility.delegate) {
				is Visibilities.Private -> append("private ")
				is Visibilities.Protected -> append("protected ")
				is Visibilities.Internal -> append("internal ")
				is Visibilities.Public -> append("public ")
			}
			when (modality) {
				FINAL -> append("sealed ")
				ABSTRACT -> append("abstract ")
				SEALED -> TODO()
				OPEN -> {}
			}
			append("enum ")
			append(name)
			appendLine()
			repeat(padding) { append("    ") }
			append("{")
			appendLine()
			append(
				declarations
					.mapNotNull { it.visit(padding + 1) }
					.joinToString("\n")
			)
			appendLine()
			repeat(padding) { append("    ") }
			append("}")
		}
	}

	fun IrClass.visitAnnotationClass(padding: Int): String {
		return buildString {
			repeat(padding) { append("    ") }
			when (visibility.delegate) {
				is Visibilities.Private -> append("private ")
				is Visibilities.Protected -> append("protected ")
				is Visibilities.Internal -> append("internal ")
				is Visibilities.Public -> append("public ")
			}
			when (modality) {
				FINAL -> append("sealed ")
				ABSTRACT -> append("abstract ")
				SEALED -> TODO()
				OPEN -> {}
			}
			append("class ")
			append(name)
			append(" : global::System.Attribute")
			appendLine()
			repeat(padding) { append("    ") }
			append("{")
			appendLine()
			append(
				declarations
					.mapNotNull { it.visit(padding + 1) }
					.joinToString("\n")
			)
			appendLine()
			repeat(padding) { append("    ") }
			append("}")
		}
	}

	fun IrClass.visitObject(padding: Int): String {
		return buildString {
			repeat(padding) { append("    ") }
			when (visibility.delegate) {
				is Visibilities.Private -> append("private ")
				is Visibilities.Protected -> append("protected ")
				is Visibilities.Internal -> append("internal ")
				is Visibilities.Public -> append("public ")
			}
			when (modality) {
				FINAL -> append("sealed ")
				ABSTRACT -> append("abstract ")
				SEALED -> TODO()
				OPEN -> {}
			}
			append("class ")
			append(name)
			append(" : ")
			append(superTypes.joinToString(", ") { typeMapper.mapType(it) })
			appendLine()
			repeat(padding) { append("    ") }
			append("{")
			appendLine()
			repeat(padding + 1) { append("    ") }
			append("public static ")
			append(typeMapper.mapType(defaultType))
			append(" INSTANCE { get; } = new ")
			append(typeMapper.mapType(defaultType))
			append("();")
			appendLine()
			append(
				declarations
					.mapNotNull { it.visit(padding + 1) }
					.joinToString("\n")
			)
			appendLine()
			repeat(padding) { append("    ") }
			append("}")
		}
	}

	fun IrDeclaration.visit(padding: Int): String? {
		if (isFakeOverride) return null
		return when (this) {
			is IrClass -> visit(padding)
			is IrFunction -> visit(padding)
			is IrProperty -> visit(padding)
			else -> buildString {
				repeat(padding + 1) { append("    ") }
				append("/* Unsupported declaration: ${this::class.java.simpleName} */")
			}
		}
	}

	fun IrFunction.visit(padding: Int) = when (this) {
		is IrConstructor -> visit(padding)
		else -> buildString {
			// 确定是否为静态方法（顶级函数为static，类方法根据标记决定）
			val isStatic = when {
				parent is IrFile -> true  // 顶级函数总是static
				isStatic -> true     // 明确标记为static的方法
				else -> false             // 默认为普通实例方法
			}

			val returnType = typeMapper.mapReturnType(returnType)

			// 方法参数
			val parameters = valueParameters.map {
				typeMapper.mapType(it.type) to it.name.asString()
			}

			// 开始生成方法声明
			repeat(padding) { append("    ") }

			when (visibility.delegate) {
				is Visibilities.Private -> append("private ")
				is Visibilities.Protected -> append("protected ")
				is Visibilities.Internal -> append("internal ")
				is Visibilities.Public -> append("public ")
			}

			// 静态构造函数或标记为静态的方法
			if (isStatic) {
				append("static ")
			}
			append("$returnType ")

			append("${name.asString()}(")
			append(parameters.joinToString(", ") { "${it.first} ${it.second}" })
			append(")")
			appendLine()
			repeat(padding) { append("    ") }
			append("{")

			// 函数体
			if (body != null) {
				body?.visit(padding + 1)?.let {
					appendLine()
					append(it)
					appendLine()
					repeat(padding) { append("    ") }
				}
			} else {
				appendLine()
				repeat(padding + 1) { append("    ") }

				// 为不同返回类型生成默认返回值
				when {
					returnType == "void" -> {} // void方法不需要返回值
					returnType == "string" -> append("return \"\";")
					returnType == "bool" -> append("return false;")
					returnType.endsWith("[]") -> append("return new $returnType {};")
					returnType == "int" || returnType == "long" || returnType == "float" || returnType == "double" ->
						append("return 0;")

					else -> append("return null;")
				}
				appendLine()
				repeat(padding) { append("    ") }
			}

			append("}")
		}
	}

	fun IrConstructor.visit(padding: Int) = buildString {
		// 确定是否为构造函数
		val className = (parent as? IrClass)?.name?.asString() ?: "Unknown"

		// 方法参数
		val parameters = valueParameters.map {
			typeMapper.mapType(it.type) to it.name.asString()
		}

		// 开始生成方法声明
		repeat(padding) { append("    ") }

		when (visibility.delegate) {
			is Visibilities.Private -> append("private ")
			is Visibilities.Protected -> append("protected ")
			is Visibilities.Internal -> append("internal ")
			is Visibilities.Public -> append("public ")
		}

		append("$className(")
		append(parameters.joinToString(", ") { "${it.first} ${it.second}" })
		append(")")
		if (body?.statements?.get(0) is IrDelegatingConstructorCall) {
			// 如果有委托调用，则添加委托调用
			append(" : ")
			append((body?.statements?.get(0) as IrDelegatingConstructorCall).visit(padding + 1))
		}

		appendLine()
		repeat(padding) { append("    ") }
		append("{")

		// 函数体
		body?.visit(padding + 1)?.let {
			appendLine()
			append(it)
		}

		val parent = parentAsClass
		parent.declarations
			.filterIsInstance<IrProperty>()
			.map { it to it.backingField?.initializer?.expression }
			.filter { it.second != null }
			.map { it.first to it.second!! }
			.forEach { (property, initializer) ->
				appendLine()
				repeat(padding + 1) { append("    ") }
				append("this.${property.name.asString()} = ${initializer.visit(padding)};")
			}

		appendLine()
		repeat(padding) { append("    ") }
		append("}")
	}

	fun IrProperty.visit(padding: Int) = buildString {
		repeat(padding) { append("    ") }

		when (visibility.delegate) {
			is Visibilities.Private -> append("private ")
			is Visibilities.Protected -> append("protected ")
			is Visibilities.Internal -> append("internal ")
			is Visibilities.Public -> append("public ")
		}

		val isStatic = when {
			parent is IrFile -> true  // 顶级函数总是static
			(backingField?.isStatic ?: getter?.isStatic ?: setter?.isStatic) == true -> true
			else -> false             // 默认为普通实例方法
		}

		if (isStatic) {
			append("static ")
		}

		when (modality) {
			FINAL -> {}
			SEALED -> TODO()
			OPEN -> append("virtual ")
			ABSTRACT -> append("abstract ")
		}

		val type = getter?.returnType
			?: setter?.returnType
			?: error("Property must have either getter or setter: $this")
		append(typeMapper.mapType(type))
		append(" ")

		append(name.asString())
		append(" ")

		append("{ ")
		if (getter != null) {
			append("get; ")
		}
		if (setter != null) {
			append("set; ")
		}
		append("}")
	}

	fun IrBody.visit(padding: Int): String? {
		return statements
			.filterNot { it is IrDelegatingConstructorCall }
			.filterNot { it is IrInstanceInitializerCall }
			.mapNotNull {
				when (it) {
					is IrExpression -> it.visit(padding)
					is IrVariable -> it.visit(padding)
					else -> "/* Unsupported statement: ${it::class.java.simpleName} */"
				}
			}
			.filter { it.isNotEmpty() }
			.joinToString("\n") {
				buildString {
					repeat(padding) { append("    ") }
					append(it)
					append(";")
				}
			}
			.let {
				when (it.isEmpty()) {
					true -> null
					else -> it
				}
			}
	}

	fun IrDelegatingConstructorCall.visit(padding: Int): String {
		return buildString {
			append("base(")
			append(
				valueArguments
					.filterNotNull()
					.mapNotNull { it.visit(padding) }
					.joinToString(", ")
			)
			append(")")
		}
	}

	fun IrGetObjectValue.visit(padding: Int): String {
		return buildString {
			append(typeMapper.mapType(symbol.owner.defaultType))
			append(".INSTANCE")
		}
	}

	fun IrConstructorCall.visit(padding: Int): String {
		return buildString {
			val constructedClass = symbol.owner.parent as IrClass
			val packageFragment = constructedClass.getPackageFragment()

			append("new global::")
			if (!packageFragment.packageFqName.isRoot) {
				append(packageFragment.packageFqName.asString())
				append(".")
			}
			append(constructedClass.name)
			append("(")
			append(
				valueArguments
					.filterNotNull()
					.mapNotNull { it.visit(padding + 1) }
					.joinToString(", ")
			)
			append(")")
		}
	}

	fun IrCall.visit(padding: Int): String {
		return buildString {
			val function = symbol.owner
			val parent = function.parent

			try {
				when (parent) {
					is IrClass -> {
						val isStatic = function.isStatic
						val isCompanionStatic = (function as? Fir2IrLazySimpleFunction)?.fir?.annotations?.any {
							it.annotationTypeRef.coneType.classId == classId("kotlin.clr", "ClrStatic")
						} == true

						when {
							isStatic -> {
								append(typeMapper.mapType(parent.defaultType))
								append(".")
								if (function.name.isSpecial) {
									val name = function.name.asString()
									when {
										name.startsWith("<set-") -> {
											append(name.substring("<set-".length, name.length - 1))
											append(" = ")
											append(valueArguments[0]!!.visit(padding + 1))
										}

										name.startsWith("<get-") -> {
											append(name.substring("<get-".length, name.length - 1))
										}

										else -> TODO(name)
									}
									return@buildString
								} else {
									append(function.name.asString())
								}
							}

							isCompanionStatic -> {
								val outer = parent.parent as? IrClass
									?: throw IllegalStateException("Expected IrClass but got ${parent::class.java}: ${parent.render()}")
								append(typeMapper.mapType(outer.defaultType))
								append(".")
								if (function.name.isSpecial) {
									val name = function.name.asString()
									when {
										name.startsWith("<set-") -> {
											append(name.substring("<set-".length, name.length - 1))
											append(" = ")
											append(valueArguments[0]!!.visit(padding + 1))
										}

										name.startsWith("<get-") -> {
											append(name.substring("<get-".length, name.length - 1))
										}

										else -> TODO(name)
									}
									return@buildString
								} else {
									append(function.name.asString())
								}
							}

							else -> {
								when {
									function.isOperator -> {
										when (function.name.asString()) {
											"plus" -> {
												append("(")
												append(arguments[0]!!.visit(padding + 1))
												append(")")
												append(" + ")
												append("(")
												append(arguments[1]!!.visit(padding + 1))
												append(")")
											}

											"times" -> {
												append("(")
												append(arguments[0]!!.visit(padding + 1))
												append(")")
												append(" * ")
												append("(")
												append(arguments[1]!!.visit(padding + 1))
												append(")")
											}

											else -> TODO(function.name.asString())
										}
										return@buildString
									}

									else -> {
										append(dispatchReceiver!!.visit(padding + 1))
										append(".")
										if (function.name.isSpecial) {
											val name = function.name.asString()
											when {
												name.startsWith("<set-") -> {
													append(name.substring("<set-".length, name.length - 1))
													append(" = ")
													append(valueArguments[0]!!.visit(padding + 1))
												}

												name.startsWith("<get-") -> {
													append(name.substring("<get-".length, name.length - 1))
												}

												else -> TODO(name)
											}
											return@buildString
										} else {
											append(function.name.asString())
										}
									}
								}
							}
						}

						append("(")
						append(
							valueArguments
								.filterNotNull()
								.mapNotNull { it.visit(padding + 1) }
								.joinToString(", ")
						)
						append(")")
					}

					else -> {
						throw IllegalStateException("Unexpected parent declaration: ${parent::class.java}: ${parent.render()}")
					}
				}
			} catch (e: Exception) {
				append("/* Error processing call: ${e.message} */")
				append(symbol.owner.name.asString())
				append("(")
				append(
					valueArguments
						.filterNotNull()
						.mapNotNull { it.visit(padding + 1) }
						.joinToString(", ")
				)
				append(")")
			}
		}
	}

	fun IrReturn.visit(padding: Int): String {
		return buildString {
			append("return ")
			append(value.visit(padding + 1))
		}
	}

	fun IrVariable.visit(padding: Int): String {
		return buildString {
			append(typeMapper.mapType(type))
			append(" ")
			append(name.asString())
			initializer?.let {
				append(" = ")
				append(it.visit(padding + 1))
			}
		}
	}

	fun IrSetValue.visit(padding: Int): String {
		return buildString {
			append(symbol.visit(padding + 1))
			append(" = ")
			append(value.visit(padding + 1))
		}
	}

	fun IrExpression.visit(padding: Int): String? {
		return when (this) {
			is IrConst -> visit(padding)
			is IrCall -> visit(padding)
			is IrStringConcatenation -> visit(padding)
			is IrGetValue -> visit(padding)
			is IrConstructorCall -> visit(padding)
			is IrGetObjectValue -> visit(padding)
			is IrReturn -> visit(padding)
			is IrSetValue -> visit(padding)
			else -> "/* Unsupported expression: ${this::class.java.simpleName} */"
		}
	}

	fun IrConst.visit(padding: Int): String {
		return when (value) {
			is String -> "\"$value\""
			is Number -> value.toString()
			is Boolean -> value.toString()
			is Char -> "'$value'"
			null -> "null"
			else -> "/* Unsupported constant type: ${(value!!)::class.java.simpleName} */"
		}
	}

	fun IrStringConcatenation.visit(padding: Int): String {
		return arguments
			.mapNotNull { it.visit(padding + 1) }
			.joinToString(" + ")
	}

	fun IrGetValue.visit(padding: Int): String {
		return symbol.visit(padding + 1)
	}

	fun IrSymbol.visit(padding: Int): String {
		return when (this) {
			is IrVariableSymbol,
			is IrValueSymbol,
				-> {
				val name = owner.name
				when (name.isSpecial) {
					true -> when (name.asString()) {
						"<this>" -> "this"
						else -> "/* Unsupported special name: $name */"
					}

					else -> owner.name.asString()
				}
			}

			else -> "/* Unsupported symbol: ${this::class.java.simpleName} */"
		}
	}
}