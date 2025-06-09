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
class Byte private constructor() : Number(), Comparable<Byte> {
	external override operator fun compareTo(other: Byte): Int
	external operator fun compareTo(other: Short): Int
	external operator fun compareTo(other: Int): Int
	external operator fun compareTo(other: Long): Int
	external operator fun compareTo(other: Float): Int
	external operator fun compareTo(other: Double): Int
	external operator fun plus(other: Byte): Int
	external operator fun plus(other: Short): Int
	external operator fun plus(other: Int): Int
	external operator fun plus(other: Long): Long
	external operator fun plus(other: Float): Float
	external operator fun plus(other: Double): Double
	external operator fun minus(other: Byte): Int
	external operator fun minus(other: Short): Int
	external operator fun minus(other: Int): Int
	external operator fun minus(other: Long): Long
	external operator fun minus(other: Float): Float
	external operator fun minus(other: Double): Double
	external operator fun times(other: Byte): Int
	external operator fun times(other: Short): Int
	external operator fun times(other: Int): Int
	external operator fun times(other: Long): Long
	external operator fun times(other: Float): Float
	external operator fun times(other: Double): Double
	external operator fun div(other: Byte): Int
	external operator fun div(other: Short): Int
	external operator fun div(other: Int): Int
	external operator fun div(other: Long): Long
	external operator fun div(other: Float): Float
	external operator fun div(other: Double): Double
	external operator fun rem(other: Byte): Int
	external operator fun rem(other: Short): Int
	external operator fun rem(other: Int): Int
	external operator fun rem(other: Long): Long
	external operator fun rem(other: Float): Float
	external operator fun rem(other: Double): Double
	external operator fun inc(): Byte
	external operator fun dec(): Byte
	external operator fun unaryPlus(): Int
	external operator fun unaryMinus(): Int
	external operator fun rangeTo(other: Byte): IntRange
	external operator fun rangeTo(other: Short): IntRange
	external operator fun rangeTo(other: Int): IntRange
	external operator fun rangeTo(other: Long): LongRange
	external operator fun rangeUntil(other: Byte): IntRange
	external operator fun rangeUntil(other: Short): IntRange
	external operator fun rangeUntil(other: Int): IntRange
	external operator fun rangeUntil(other: Long): LongRange
	external override fun toByte(): Byte
	external override fun toChar(): Char
	external override fun toShort(): Short
	external override fun toInt(): Int
	external override fun toLong(): Long
	external override fun toFloat(): Float
	external override fun toDouble(): Double
	external override fun equals(other: Any?): Boolean
	external override fun toString(): String
	companion object {
		const val MIN_VALUE: Byte = -128
		const val MAX_VALUE: Byte = 127
		const val SIZE_BYTES: Int = 1
		const val SIZE_BITS: Int = 8
	}
}

@ActualizeByClrBuiltinProvider
class Short private constructor() : Number(), Comparable<Short> {
	external operator fun compareTo(other: Byte): Int
	external override operator fun compareTo(other: Short): Int
	external operator fun compareTo(other: Int): Int
	external operator fun compareTo(other: Long): Int
	external operator fun compareTo(other: Float): Int
	external operator fun compareTo(other: Double): Int
	external operator fun plus(other: Byte): Int
	external operator fun plus(other: Short): Int
	external operator fun plus(other: Int): Int
	external operator fun plus(other: Long): Long
	external operator fun plus(other: Float): Float
	external operator fun plus(other: Double): Double
	external operator fun minus(other: Byte): Int
	external operator fun minus(other: Short): Int
	external operator fun minus(other: Int): Int
	external operator fun minus(other: Long): Long
	external operator fun minus(other: Float): Float
	external operator fun minus(other: Double): Double
	external operator fun times(other: Byte): Int
	external operator fun times(other: Short): Int
	external operator fun times(other: Int): Int
	external operator fun times(other: Long): Long
	external operator fun times(other: Float): Float
	external operator fun times(other: Double): Double
	external operator fun div(other: Byte): Int
	external operator fun div(other: Short): Int
	external operator fun div(other: Int): Int
	external operator fun div(other: Long): Long
	external operator fun div(other: Float): Float
	external operator fun div(other: Double): Double
	external operator fun rem(other: Byte): Int
	external operator fun rem(other: Short): Int
	external operator fun rem(other: Int): Int
	external operator fun rem(other: Long): Long
	external operator fun rem(other: Float): Float
	external operator fun rem(other: Double): Double
	external operator fun inc(): Short
	external operator fun dec(): Short
	external operator fun unaryPlus(): Int
	external operator fun unaryMinus(): Int
	external operator fun rangeTo(other: Byte): IntRange
	external operator fun rangeTo(other: Short): IntRange
	external operator fun rangeTo(other: Int): IntRange
	external operator fun rangeTo(other: Long): LongRange
	external operator fun rangeUntil(other: Byte): IntRange
	external operator fun rangeUntil(other: Short): IntRange
	external operator fun rangeUntil(other: Int): IntRange
	external operator fun rangeUntil(other: Long): LongRange
	external override fun toByte(): Byte
	external override fun toChar(): Char
	external override fun toShort(): Short
	external override fun toInt(): Int
	external override fun toLong(): Long
	external override fun toFloat(): Float
	external override fun toDouble(): Double
	external override fun equals(other: Any?): Boolean
	external override fun toString(): String
	companion object {
		const val MIN_VALUE: Short = -32768
		const val MAX_VALUE: Short = 32767
		const val SIZE_BYTES: Int = 2
		const val SIZE_BITS: Int = 16
	}
}

@ActualizeByClrBuiltinProvider
class Int private constructor() : Number(), Comparable<Int> {
	external operator fun compareTo(other: Byte): Int
	external operator fun compareTo(other: Short): Int
	external override operator fun compareTo(other: Int): Int
	external operator fun compareTo(other: Long): Int
	external operator fun compareTo(other: Float): Int
	external operator fun compareTo(other: Double): Int
	external operator fun plus(other: Byte): Int
	external operator fun plus(other: Short): Int
	external operator fun plus(other: Int): Int
	external operator fun plus(other: Long): Long
	external operator fun plus(other: Float): Float
	external operator fun plus(other: Double): Double
	external operator fun minus(other: Byte): Int
	external operator fun minus(other: Short): Int
	external operator fun minus(other: Int): Int
	external operator fun minus(other: Long): Long
	external operator fun minus(other: Float): Float
	external operator fun minus(other: Double): Double
	external operator fun times(other: Byte): Int
	external operator fun times(other: Short): Int
	external operator fun times(other: Int): Int
	external operator fun times(other: Long): Long
	external operator fun times(other: Float): Float
	external operator fun times(other: Double): Double
	external operator fun div(other: Byte): Int
	external operator fun div(other: Short): Int
	external operator fun div(other: Int): Int
	external operator fun div(other: Long): Long
	external operator fun div(other: Float): Float
	external operator fun div(other: Double): Double
	external operator fun rem(other: Byte): Int
	external operator fun rem(other: Short): Int
	external operator fun rem(other: Int): Int
	external operator fun rem(other: Long): Long
	external operator fun rem(other: Float): Float
	external operator fun rem(other: Double): Double
	external operator fun inc(): Int
	external operator fun dec(): Int
	external operator fun unaryPlus(): Int
	external operator fun unaryMinus(): Int
	external operator fun rangeTo(other: Byte): IntRange
	external operator fun rangeTo(other: Short): IntRange
	external operator fun rangeTo(other: Int): IntRange
	external operator fun rangeTo(other: Long): LongRange
	external operator fun rangeUntil(other: Byte): IntRange
	external operator fun rangeUntil(other: Short): IntRange
	external operator fun rangeUntil(other: Int): IntRange
	external operator fun rangeUntil(other: Long): LongRange
	external infix fun shl(bitCount: Int): Int
	external infix fun shr(bitCount: Int): Int
	external infix fun ushr(bitCount: Int): Int
	external infix fun and(other: Int): Int
	external infix fun or(other: Int): Int
	external infix fun xor(other: Int): Int
	external fun inv(): Int
	external override fun toByte(): Byte
	external override fun toChar(): Char
	external override fun toShort(): Short
	external override fun toInt(): Int
	external override fun toLong(): Long
	external override fun toFloat(): Float
	external override fun toDouble(): Double
	external override fun equals(other: Any?): Boolean
	external override fun toString(): String
	companion object {
		const val MIN_VALUE: Int = -2147483648
		const val MAX_VALUE: Int = 2147483647
		const val SIZE_BYTES: Int = 4
		const val SIZE_BITS: Int = 32
	}
}

@ActualizeByClrBuiltinProvider
class Long private constructor() : Number(), Comparable<Long> {
	external operator fun compareTo(other: Byte): Int
	external operator fun compareTo(other: Short): Int
	external operator fun compareTo(other: Int): Int
	external override operator fun compareTo(other: Long): Int
	external operator fun compareTo(other: Float): Int
	external operator fun compareTo(other: Double): Int
	external operator fun plus(other: Byte): Long
	external operator fun plus(other: Short): Long
	external operator fun plus(other: Int): Long
	external operator fun plus(other: Long): Long
	external operator fun plus(other: Float): Float
	external operator fun plus(other: Double): Double
	external operator fun minus(other: Byte): Long
	external operator fun minus(other: Short): Long
	external operator fun minus(other: Int): Long
	external operator fun minus(other: Long): Long
	external operator fun minus(other: Float): Float
	external operator fun minus(other: Double): Double
	external operator fun times(other: Byte): Long
	external operator fun times(other: Short): Long
	external operator fun times(other: Int): Long
	external operator fun times(other: Long): Long
	external operator fun times(other: Float): Float
	external operator fun times(other: Double): Double
	external operator fun div(other: Byte): Long
	external operator fun div(other: Short): Long
	external operator fun div(other: Int): Long
	external operator fun div(other: Long): Long
	external operator fun div(other: Float): Float
	external operator fun div(other: Double): Double
	external operator fun rem(other: Byte): Long
	external operator fun rem(other: Short): Long
	external operator fun rem(other: Int): Long
	external operator fun rem(other: Long): Long
	external operator fun rem(other: Float): Float
	external operator fun rem(other: Double): Double
	external operator fun inc(): Long
	external operator fun dec(): Long
	external operator fun unaryPlus(): Long
	external operator fun unaryMinus(): Long
	external operator fun rangeTo(other: Byte): LongRange
	external operator fun rangeTo(other: Short): LongRange
	external operator fun rangeTo(other: Int): LongRange
	external operator fun rangeTo(other: Long): LongRange
	external operator fun rangeUntil(other: Byte): LongRange
	external operator fun rangeUntil(other: Short): LongRange
	external operator fun rangeUntil(other: Int): LongRange
	external operator fun rangeUntil(other: Long): LongRange
	external infix fun shl(bitCount: Int): Long
	external infix fun shr(bitCount: Int): Long
	external infix fun ushr(bitCount: Int): Long
	external infix fun and(other: Long): Long
	external infix fun or(other: Long): Long
	external infix fun xor(other: Long): Long
	external fun inv(): Long
	external override fun toByte(): Byte
	external override fun toChar(): Char
	external override fun toShort(): Short
	external override fun toInt(): Int
	external override fun toLong(): Long
	external override fun toFloat(): Float
	external override fun toDouble(): Double
	external override fun equals(other: Any?): Boolean
	external override fun toString(): String
	companion object {
		const val MIN_VALUE: Long = -9223372036854775807L - 1L
		const val MAX_VALUE: Long = 9223372036854775807L
		const val SIZE_BYTES: Int = 8
		const val SIZE_BITS: Int = 64
	}
}

@ActualizeByClrBuiltinProvider
class Float private constructor() : Number(), Comparable<Float> {
	external operator fun compareTo(other: Byte): Int
	external operator fun compareTo(other: Short): Int
	external operator fun compareTo(other: Int): Int
	external operator fun compareTo(other: Long): Int
	external override operator fun compareTo(other: Float): Int
	external operator fun compareTo(other: Double): Int
	external operator fun plus(other: Byte): Float
	external operator fun plus(other: Short): Float
	external operator fun plus(other: Int): Float
	external operator fun plus(other: Long): Float
	external operator fun plus(other: Float): Float
	external operator fun plus(other: Double): Double
	external operator fun minus(other: Byte): Float
	external operator fun minus(other: Short): Float
	external operator fun minus(other: Int): Float
	external operator fun minus(other: Long): Float
	external operator fun minus(other: Float): Float
	external operator fun minus(other: Double): Double
	external operator fun times(other: Byte): Float
	external operator fun times(other: Short): Float
	external operator fun times(other: Int): Float
	external operator fun times(other: Long): Float
	external operator fun times(other: Float): Float
	external operator fun times(other: Double): Double
	external operator fun div(other: Byte): Float
	external operator fun div(other: Short): Float
	external operator fun div(other: Int): Float
	external operator fun div(other: Long): Float
	external operator fun div(other: Float): Float
	external operator fun div(other: Double): Double
	external operator fun rem(other: Byte): Float
	external operator fun rem(other: Short): Float
	external operator fun rem(other: Int): Float
	external operator fun rem(other: Long): Float
	external operator fun rem(other: Float): Float
	external operator fun rem(other: Double): Double
	external operator fun inc(): Float
	external operator fun dec(): Float
	external operator fun unaryPlus(): Float
	external operator fun unaryMinus(): Float
	external override fun toByte(): Byte
	external override fun toChar(): Char
	external override fun toShort(): Short
	external override fun toInt(): Int
	external override fun toLong(): Long
	external override fun toFloat(): Float
	external override fun toDouble(): Double
	external override fun equals(other: Any?): Boolean
	external override fun toString(): String
	companion object {
		const val MIN_VALUE: Float = 1.4E-45F
		const val MAX_VALUE: Float = 3.4028235E38F
		const val POSITIVE_INFINITY: Float = 1.0F/0.0F
		const val NEGATIVE_INFINITY: Float = -1.0F/0.0F
		const val NaN: Float = -(0.0F/0.0F)
		const val SIZE_BYTES: Int = 4
		const val SIZE_BITS: Int = 32
	}
}

@ActualizeByClrBuiltinProvider
class Double private constructor() : Number(), Comparable<Double> {
	external operator fun compareTo(other: Byte): Int
	external operator fun compareTo(other: Short): Int
	external operator fun compareTo(other: Int): Int
	external operator fun compareTo(other: Long): Int
	external operator fun compareTo(other: Float): Int
	external override operator fun compareTo(other: Double): Int
	external operator fun plus(other: Byte): Double
	external operator fun plus(other: Short): Double
	external operator fun plus(other: Int): Double
	external operator fun plus(other: Long): Double
	external operator fun plus(other: Float): Double
	external operator fun plus(other: Double): Double
	external operator fun minus(other: Byte): Double
	external operator fun minus(other: Short): Double
	external operator fun minus(other: Int): Double
	external operator fun minus(other: Long): Double
	external operator fun minus(other: Float): Double
	external operator fun minus(other: Double): Double
	external operator fun times(other: Byte): Double
	external operator fun times(other: Short): Double
	external operator fun times(other: Int): Double
	external operator fun times(other: Long): Double
	external operator fun times(other: Float): Double
	external operator fun times(other: Double): Double
	external operator fun div(other: Byte): Double
	external operator fun div(other: Short): Double
	external operator fun div(other: Int): Double
	external operator fun div(other: Long): Double
	external operator fun div(other: Float): Double
	external operator fun div(other: Double): Double
	external operator fun rem(other: Byte): Double
	external operator fun rem(other: Short): Double
	external operator fun rem(other: Int): Double
	external operator fun rem(other: Long): Double
	external operator fun rem(other: Float): Double
	external operator fun rem(other: Double): Double
	external operator fun inc(): Double
	external operator fun dec(): Double
	external operator fun unaryPlus(): Double
	external operator fun unaryMinus(): Double
	external override fun toByte(): Byte
	external override fun toChar(): Char
	external override fun toShort(): Short
	external override fun toInt(): Int
	external override fun toLong(): Long
	external override fun toFloat(): Float
	external override fun toDouble(): Double
	external override fun equals(other: Any?): Boolean
	external override fun toString(): String
	companion object {
		const val MIN_VALUE: Double = 4.9E-324
		const val MAX_VALUE: Double = 1.7976931348623157E308
		const val POSITIVE_INFINITY: Double = 1.0/0.0
		const val NEGATIVE_INFINITY: Double = -1.0/0.0
		const val NaN: Double = -(0.0/0.0)
		const val SIZE_BYTES: Int = 8
		const val SIZE_BITS: Int = 64
	}
}