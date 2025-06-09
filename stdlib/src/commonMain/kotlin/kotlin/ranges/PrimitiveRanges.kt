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

package kotlin.ranges

class CharRange(start: Char, endInclusive: Char) : CharProgression(start, endInclusive, 1), ClosedRange<Char>, OpenEndRange<Char> {
	override val start: Char get() = first
	override val endInclusive: Char get() = last

	@Deprecated("Can throw an exception when it's impossible to represent the value with Char type, for example, when the range includes MAX_VALUE. It's recommended to use 'endInclusive' property that doesn't throw.")
	@SinceKotlin("1.9")
	override val endExclusive: Char
		external get

	external override fun contains(value: Char): Boolean
	external override fun isEmpty(): Boolean
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	external override fun toString(): String
	companion object {
		val EMPTY: CharRange = CharRange(1.toChar(), 0.toChar())
	}
}

class IntRange(start: Int, endInclusive: Int) : IntProgression(start, endInclusive, 1), ClosedRange<Int>, OpenEndRange<Int> {
	override val start: Int get() = first
	override val endInclusive: Int get() = last

	@Deprecated("Can throw an exception when it's impossible to represent the value with Int type, for example, when the range includes MAX_VALUE. It's recommended to use 'endInclusive' property that doesn't throw.")
	@SinceKotlin("1.9")
	override val endExclusive: Int
		external get

	external override fun contains(value: Int): Boolean
	external override fun isEmpty(): Boolean
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	external override fun toString(): String
	companion object {
		val EMPTY: IntRange = IntRange(1, 0)
	}
}

class LongRange(start: Long, endInclusive: Long) : LongProgression(start, endInclusive, 1), ClosedRange<Long>, OpenEndRange<Long> {
	override val start: Long get() = first
	override val endInclusive: Long get() = last

	@Deprecated("Can throw an exception when it's impossible to represent the value with Long type, for example, when the range includes MAX_VALUE. It's recommended to use 'endInclusive' property that doesn't throw.")
	override val endExclusive: Long
		external get

	external override fun contains(value: Long): Boolean
	external override fun isEmpty(): Boolean
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	external override fun toString(): String
	companion object {
		val EMPTY: LongRange = LongRange(1, 0)
	}
}