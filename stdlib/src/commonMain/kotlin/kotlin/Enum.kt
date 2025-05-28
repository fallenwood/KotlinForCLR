package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
abstract class Enum<E : Enum<E>>(name: String, ordinal: Int): Comparable<E> {
	val name: String = name
	val ordinal: Int = ordinal

	external override fun compareTo(other: E): Int
	protected external fun clone(): Any
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	external override fun toString(): String

	companion object
}