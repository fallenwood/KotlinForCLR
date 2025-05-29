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
import org.jetbrains.kotlin.name.SpecialNames

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

	fun IrClass.visit(padding: Int) = buildString {
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
		when (kind) {
			CLASS -> append(visitClass(padding))
			INTERFACE -> append(visitInterface(padding))
			ENUM_CLASS -> append(visitEnumClass(padding))
			ENUM_ENTRY -> TODO()
			ANNOTATION_CLASS -> append(visitAnnotationClass(padding))
			OBJECT -> append(visitObject(padding))
		}
	}

	fun IrClass.visitClass(padding: Int): String {
		return buildString {
			append("class ")
			append(name)
			appendLine()
			repeat(padding) { append("    ") }
			append("{")
			appendLine()
			append(
				declarations.filterIsInstance<IrFunction>()
					.filterNot { it.isFakeOverride }
					.joinToString("\n") { it.visit(padding + 1) }
			)
			appendLine()
			repeat(padding) { append("    ") }
			append("}")
		}
	}

	fun IrClass.visitInterface(padding: Int): String {
		return buildString {
			append("interface ")
			append(name)
			appendLine()
			repeat(padding) { append("    ") }
			append("{")
			appendLine()
			append(
				declarations.filterIsInstance<IrFunction>()
					.filterNot { it.isFakeOverride }
					.joinToString("\n") { it.visit(padding + 1) }
			)
			appendLine()
			repeat(padding) { append("    ") }
			append("}")
		}
	}

	fun IrClass.visitEnumClass(padding: Int): String {
		return buildString {
			append("enum ")
			append(name)
			appendLine()
			repeat(padding) { append("    ") }
			append("{")
			appendLine()
			append(
				declarations.filterIsInstance<IrFunction>()
					.filterNot { it.isFakeOverride }
					.joinToString("\n") { it.visit(padding + 1) }
			)
			appendLine()
			repeat(padding) { append("    ") }
			append("}")
		}
	}

	fun IrClass.visitAnnotationClass(padding: Int): String {
		return buildString {
			append("class ")
			append(name)
			append(" : global::System.Attribute")
			appendLine()
			repeat(padding) { append("    ") }
			append("{")
			appendLine()
			append(
				declarations.filterIsInstance<IrFunction>()
					.filterNot { it.isFakeOverride }
					.joinToString("\n") { it.visit(padding + 1) }
			)
			appendLine()
			repeat(padding) { append("    ") }
			append("}")
		}
	}

	fun IrClass.visitObject(padding: Int): String {
		return buildString {
			append("class ")
			append(name)
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
				declarations.filterIsInstance<IrFunction>()
					.filterNot { it.isFakeOverride }
					.joinToString("\n") { it.visit(padding + 1) }
			)
			appendLine()
			repeat(padding) { append("    ") }
			append("}")
		}
	}

	fun IrFunction.visit(padding: Int) = buildString {
		// 确定是否为构造函数
		val isConstructor = this@visit is IrConstructor || name == SpecialNames.INIT
		val className = (parent as? IrClass)?.name?.asString() ?: "Unknown"

		// 确定方法名
		val methodName = when {
			isConstructor -> className
			else -> name.asString()
		}

		// 确定是否为静态方法（顶级函数为static，类方法根据标记决定）
		val isStatic = when {
			parent is IrFile -> true  // 顶级函数总是static
			isConstructor -> false    // 普通构造函数不是static
			isStatic -> true     // 明确标记为static的方法
			else -> false             // 默认为普通实例方法
		}

		// 根据构造函数与否决定返回类型
		val returnType = if (!isConstructor) typeMapper.mapReturnType(returnType) else ""

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

		// 仅对非构造函数添加返回类型
		if (!isConstructor) {
			append("$returnType ")
		}

		append("$methodName(")
		append(parameters.joinToString(", ") { "${it.first} ${it.second}" })
		append(")")
		appendLine()
		repeat(padding) { append("    ") }
		append("{")

		// 函数体
		appendLine()
		if (body != null) {
			append(body?.visit(padding + 1) ?: "")
		} else {
			repeat(padding + 1) { append("    ") }

			// 为不同返回类型生成默认返回值
			when {
				isConstructor -> {}  // 构造函数不需要返回值
				returnType == "void" -> {} // void方法不需要返回值
				returnType == "string" -> append("return \"\";")
				returnType == "bool" -> append("return false;")
				returnType.endsWith("[]") -> append("return new $returnType {};")
				returnType == "int" || returnType == "long" || returnType == "float" || returnType == "double" ->
					append("return 0;")

				else -> append("return null;")
			}
		}
		appendLine()

		// 方法结束
		repeat(padding) { append("    ") }
		append("}")
	}

	fun IrBody.visit(padding: Int): String {
		return statements.joinToString("\n") {
			buildString {
				repeat(padding) { append("    ") }

				append(
					when (it) {
						is IrCall -> it.visit(padding)
						is IrReturn -> it.visit(padding)
						is IrVariable -> it.visit(padding)
						is IrDelegatingConstructorCall -> it.visit(padding)
						is IrConstructorCall -> it.visit(padding)
						is IrInstanceInitializerCall -> it.visit(padding)
						else -> "/* Unsupported statement: ${it::class.java.simpleName} */"
					}
				)
				append(";")
			}
		}
	}

	fun IrInstanceInitializerCall.visit(padding: Int): String {
		return buildString {
			append("/* 实例初始化 */")
		}
	}

	fun IrGetObjectValue.visit(padding: Int): String {
		return buildString {
			append(typeMapper.mapType(symbol.owner.defaultType))
			append(".INSTANCE")
		}
	}

	fun IrDelegatingConstructorCall.visit(padding: Int): String {
		return buildString {
			append("/* 父类构造调用 */")
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
			append(valueArguments.filterNotNull().joinToString(", ") { it.visit(padding + 1) })
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
								append(function.name.asString())
							}

							isCompanionStatic -> {
								val outer = parent.parent as? IrClass
									?: throw IllegalStateException("Expected IrClass but got ${parent::class.java}: ${parent.render()}")
								append(typeMapper.mapType(outer.defaultType))
								append(".")
								append(function.name.asString())
							}

							else -> {
								when {
									function.isOperator -> {
										when (function.name.asString()) {
											"plus" -> {
												append(
													arguments
														.filterNotNull()
														.joinToString(" + ") { it.visit(padding + 1) }
												)
											}

											else -> TODO(function.name.asString())
										}
										return@buildString
									}

									else -> {
										append(dispatchReceiver!!.visit(padding + 1))
										append(".")
										append(function.name.asString())
									}
								}
							}
						}

						append("(")
						append(valueArguments.filterNotNull().joinToString(", ") { it.visit(padding + 1) })
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
				append(valueArguments.filterNotNull().joinToString(", ") { it.visit(padding + 1) })
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

	fun IrExpression.visit(padding: Int): String {
		return when (this) {
			is IrConst -> visit(padding)
			is IrCall -> visit(padding)
			is IrStringConcatenation -> visit(padding)
			is IrGetValue -> visit(padding)
			is IrDelegatingConstructorCall -> visit(padding)
			is IrConstructorCall -> visit(padding)
			is IrInstanceInitializerCall -> visit(padding)
			is IrGetObjectValue -> visit(padding)
			else -> "/* Unsupported expression: ${this::class.java.simpleName} */"
		}
	}

	fun IrConst.visit(padding: Int): String {
		return when (value) {
			is String -> "\"$value\""
			is Int -> value.toString()
			null -> "null"
			else -> "/* Unsupported constant type: ${(value!!)::class.java.simpleName} */"
		}
	}

	fun IrStringConcatenation.visit(padding: Int): String {
		return buildString {
			append(arguments.joinToString(" + ") { it.visit(padding + 1) })
		}
	}

	fun IrGetValue.visit(padding: Int): String {
		return buildString {
			append(symbol.visit(padding + 1))
		}
	}

	fun IrSymbol.visit(padding: Int): String {
		return when (this) {
			is IrVariableSymbol -> {
				owner.name.asString()
			}

			is IrValueSymbol -> {
				owner.name.asString()
			}

			else -> "/* Unsupported symbol: ${this::class.java.simpleName} */"
		}
	}
}