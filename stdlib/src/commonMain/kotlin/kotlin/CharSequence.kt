package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
interface CharSequence {
	val length: Int
	operator fun get(index: Int): Char
	fun subSequence(startIndex: Int, endIndex: Int): CharSequence
}