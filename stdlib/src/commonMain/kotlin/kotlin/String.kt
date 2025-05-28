package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
class String : CharSequence {
	override val length: Int
		external get

	external override fun get(index: Int): Char
	external override fun subSequence(startIndex: Int, endIndex: Int): CharSequence
	external override fun equals(other: Any?): Boolean
	external override fun toString(): String
}