package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
interface Comparable<in T> {
	operator fun compareTo(other: T): Int
}