package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
class Array<T> {
	@Suppress("WRONG_MODIFIER_TARGET")
	inline constructor(size: Int, init: (Int) -> Double)
	external operator fun get(index: Int): T
	external operator fun set(index: Int, value: T)
	@Suppress("MUST_BE_INITIALIZED_OR_BE_ABSTRACT")
	val size: Int
	external operator fun iterator(): Iterator<T>
}
