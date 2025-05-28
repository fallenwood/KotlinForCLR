package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
class Boolean private constructor() : Comparable<Boolean> {
	external operator fun not(): Boolean
	external infix fun and(other: Boolean): Boolean
	external infix fun or(other: Boolean): Boolean
	external infix fun xor(other: Boolean): Boolean
	external override fun compareTo(other: Boolean): Int
	external override fun toString(): String
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	companion object
}