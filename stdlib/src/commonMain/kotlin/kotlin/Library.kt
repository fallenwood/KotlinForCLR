/*
   Copyright 2025 Nyayurin

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

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