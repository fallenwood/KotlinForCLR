package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
external fun Any?.toString(): String

@ActualizeByClrBuiltinProvider
external operator fun String?.plus(other: Any?): String

@Suppress("REIFIED_TYPE_PARAMETER_NO_INLINE")
@ActualizeByClrBuiltinProvider
external fun <reified T> arrayOfNulls(size: Int): Array<T?>

@ActualizeByClrBuiltinProvider
inline fun <reified T> arrayOf(vararg elements: T): Array<T> = throw Throwable()

@ActualizeByClrBuiltinProvider
external fun doubleArrayOf(vararg elements: Double): DoubleArray

@ActualizeByClrBuiltinProvider
external fun floatArrayOf(vararg elements: Float): FloatArray
@ActualizeByClrBuiltinProvider

external fun longArrayOf(vararg elements: Long): LongArray

@ActualizeByClrBuiltinProvider
external fun intArrayOf(vararg elements: Int): IntArray

@ActualizeByClrBuiltinProvider
external fun charArrayOf(vararg elements: Char): CharArray

@ActualizeByClrBuiltinProvider
external fun shortArrayOf(vararg elements: Short): ShortArray

@ActualizeByClrBuiltinProvider
external fun byteArrayOf(vararg elements: Byte): ByteArray

@ActualizeByClrBuiltinProvider
external fun booleanArrayOf(vararg elements: Boolean): BooleanArray

@ActualizeByClrBuiltinProvider
inline fun <reified T : Enum<T>> enumValues(): Array<T> = throw Throwable()

@ActualizeByClrBuiltinProvider
inline fun <reified T : Enum<T>> enumValueOf(name: String): T = throw Throwable()