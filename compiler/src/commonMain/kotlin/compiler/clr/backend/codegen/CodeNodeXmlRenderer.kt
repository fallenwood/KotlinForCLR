package compiler.clr.backend.codegen

fun CodeNode.render() = Renderer().run { render(0) }

private class Renderer {
	fun CodeNode.render(padding: Int): String = when (this) {
		is CodeNode.None -> render(padding)
		is CodeNode.SingleLineList -> render(padding)
		is CodeNode.MultiLineList -> render(padding)
		is CodeNode.SingleLine -> render(padding)
		is CodeNode.MultiLine -> render(padding)
		is CodeNode.StringConcatenation -> render(padding)
		is PlainNode.Plain -> render(padding)
		is PlainNode.SingleLine -> render(padding)
		is PlainNode.MultiLine -> render(padding)
		is PaddingNode.If -> render(padding)
		is PaddingNode.Block -> render(padding)
	}

	private fun CodeNode.None.render(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		append("<CodeNode.None />")
	}

	private fun CodeNode.SingleLineList.render(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		appendLine("<CodeNode.CodeList>")
		appendLine(nodes.joinToString("\n") { it.render(padding + 1) })
		repeat(padding) { append("    ") }
		append("</CodeNode.CodeList>")
	}

	private fun CodeNode.MultiLineList.render(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		appendLine("<CodeNode.MultiLineList>")
		appendLine(nodes.joinToString("\n") { it.render(padding + 1) })
		repeat(padding) { append("    ") }
		append("</CodeNode.MultiLineList>")
	}

	private fun CodeNode.SingleLine.render(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		appendLine("<CodeNode.SingleLine>")
		appendLine(nodes.joinToString("\n") { it.render(padding + 1) })
		repeat(padding) { append("    ") }
		append("</CodeNode.SingleLine>")
	}

	private fun CodeNode.MultiLine.render(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		appendLine("<CodeNode.MultiLine>")
		appendLine(nodes.joinToString("\n") { it.render(padding + 1) })
		repeat(padding) { append("    ") }
		append("</CodeNode.MultiLine>")
	}

	private fun CodeNode.StringConcatenation.render(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		appendLine("<CodeNode.StringConcatenation>")
		appendLine(nodes.joinToString("\n") { it.render(padding + 1) })
		repeat(padding) { append("    ") }
		append("</CodeNode.StringConcatenation>")
	}

	private fun PlainNode.Plain.render(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		append("<PlainNode.Plain>")
		append(text)
		append("</PlainNode.Plain>")
	}

	private fun PlainNode.SingleLine.render(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		appendLine("<PlainNode.SingleLine>")
		appendLine(nodes.joinToString("\n") { it.render(padding + 1) })
		repeat(padding) { append("    ") }
		append("</PlainNode.SingleLine>")
	}

	private fun PlainNode.MultiLine.render(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		appendLine("<PlainNode.MultiLine>")
		appendLine(nodes.joinToString("\n") { it.render(padding + 1) })
		repeat(padding) { append("    ") }
		append("</PlainNode.MultiLine>")
	}

	private fun PaddingNode.If.render(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		appendLine("<PaddingNode.If else=\"$`else`\">")
		repeat(padding + 1) { append("    ") }
		appendLine("<condition>")
		appendLine(condition.render(padding + 2))
		repeat(padding + 1) { append("    ") }
		appendLine("</condition>")
		repeat(padding + 1) { append("    ") }
		appendLine("<content>")
		appendLine(content.render(padding + 2))
		repeat(padding + 1) { append("    ") }
		appendLine("</content>")
		repeat(padding) { append("    ") }
		append("</PaddingNode.If>")
	}

	private fun PaddingNode.Block.render(padding: Int) = buildString {
		repeat(padding) { append("    ") }
		appendLine("<PaddingNode.Block>")
		appendLine(nodes.joinToString("\n") { it.render(padding + 1) })
		repeat(padding) { append("    ") }
		append("</PaddingNode.Block>")
	}
}