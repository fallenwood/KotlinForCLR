package compiler.clr.backend.codegen

fun CodeNode.visit() = Visitor().run { visit(0) }

private class Visitor {
	fun CodeNode.visit(padding: Int): String = when (this) {
		is CodeNode.None -> ""
		is CodeNode.SingleLineList -> visit(padding)
		is CodeNode.MultiLineList -> visit(padding)
		is CodeNode.SingleLine -> visit(padding)
		is CodeNode.MultiLine -> visit(padding)
		is CodeNode.StringConcatenation -> visit(padding)
		is PlainNode.Plain -> visit(padding)
		is PlainNode.SingleLine -> visit(padding)
		is PlainNode.MultiLine -> visit(padding)
		is PaddingNode.If -> visit(padding)
		is PaddingNode.Block -> visit(padding)
	}

	private fun CodeNode.SingleLineList.visit(padding: Int) = buildString {
		append(nodes.joinToString("") { it.visit(padding) })
	}

	private fun CodeNode.MultiLineList.visit(padding: Int) = buildString {
		append(nodes.joinToString("\n") { it.visit(padding) })
	}

	private fun CodeNode.SingleLine.visit(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		append(nodes.joinToString("") { it.visit(padding) })
	}

	private fun CodeNode.MultiLine.visit(padding: Int) = nodes.joinToString("\n") {
		buildString {
			when (it) {
				is CodeNode.None,
				is CodeNode.SingleLine,
				is PaddingNode.Block,
					-> {
				}

				else -> repeat(padding) { append("    ") }
			}
			append(it.visit(padding))
		}
	}

	private fun CodeNode.StringConcatenation.visit(padding: Int) = nodes.joinToString("", "$\"", "\"") {
		"{(${it.visit(padding)})}"
	}

	private fun PlainNode.Plain.visit(padding: Int) = text

	private fun PlainNode.SingleLine.visit(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		append(nodes.joinToString("") { it.visit(padding) })
	}

	private fun PlainNode.MultiLine.visit(padding: Int) = nodes.joinToString("\n") {
		buildString {
			repeat(padding) { append("    ") }
			append(it.visit(padding))
		}
	}

	private fun PaddingNode.If.visit(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		if (`else`) {
			append("else ")
		}
		append("if (")
		append(condition.visit(padding))
		append(")")
		appendLine()
		append(content.visit(padding))
	}

	private fun PaddingNode.Block.visit(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		append("{")
		appendLine()
		append(nodes.joinToString("\n") { it.visit(padding + 1) })
		appendLine()
		repeat(padding) { append("    ") }
		append("}")
	}
}