package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
open class Any {
	open external operator fun equals(other: Any?): Boolean
	open external fun hashCode(): Int
	open external fun toString(): String
}