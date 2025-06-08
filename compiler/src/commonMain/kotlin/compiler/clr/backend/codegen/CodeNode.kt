package compiler.clr.backend.codegen

sealed class CodeNode {
	data object None : CodeNode()
	data class SingleLineList(val nodes: List<CodeNode>) : CodeNode()
	data class MultiLineList(val nodes: List<CodeNode>) : CodeNode()
	data class SingleLine(val nodes: List<CodeNode>) : CodeNode()
	data class MultiLine(val nodes: List<CodeNode>) : CodeNode()
	data class StringConcatenation(val nodes: List<CodeNode>) : CodeNode()
}

sealed class PlainNode : CodeNode() {
	data class Plain(val text: String) : PlainNode()
	data class SingleLine(val nodes: List<Plain>) : PlainNode()
	data class MultiLine(val nodes: List<Plain>) : PlainNode()
}

sealed class PaddingNode : CodeNode() {
	data class If(
		val `else`: Boolean,
		val condition: CodeNode,
		val content: CodeNode,
	) : PaddingNode()

	data class Block(
		val nodes: List<CodeNode>,
	) : PaddingNode()
}

val noneCode = CodeNode.None

fun singleLineListCode(vararg nodes: CodeNode) = singleLineListCode(nodes.toList())
fun singleLineListCode(nodes: List<CodeNode>) = CodeNode.SingleLineList(nodes)

fun multiLineListCode(vararg nodes: CodeNode) = multiLineListCode(nodes.toList())
fun multiLineListCode(nodes: List<CodeNode>) = CodeNode.MultiLineList(nodes)

fun singleLineCode(vararg nodes: CodeNode) = singleLineCode(nodes.toList())
fun singleLineCode(nodes: List<CodeNode>) = CodeNode.SingleLine(nodes)

fun multiLineCode(vararg nodes: CodeNode) = multiLineCode(nodes.toList())
fun multiLineCode(nodes: List<CodeNode>) = CodeNode.MultiLine(nodes)

fun stringConcatenationCode(nodes: List<CodeNode>) = CodeNode.StringConcatenation(nodes)

fun plainPlain(text: String) = PlainNode.Plain(text)

fun singleLinePlain(vararg nodes: String) = singleLinePlain(nodes.toList())
fun singleLinePlain(nodes: List<String>) = PlainNode.SingleLine(nodes.map { plainPlain(it) })

fun multiLinePlain(vararg nodes: String) = multiLinePlain(nodes.toList())
fun multiLinePlain(nodes: List<String>) = PlainNode.MultiLine(nodes.map { plainPlain(it) })

fun ifPadding(`else`: Boolean, condition: CodeNode, content: CodeNode) = PaddingNode.If(`else`, condition, content)

fun blockPadding(vararg nodes: CodeNode) = blockPadding(nodes.toList())
fun blockPadding(nodes: List<CodeNode>) = PaddingNode.Block(nodes)

fun CodeNode.appendSingleLine(vararg appends: CodeNode) = when (this) {
	is CodeNode.SingleLine -> singleLineCode(*nodes.toTypedArray(), *appends)
	else -> singleLineCode(this, *appends)
}