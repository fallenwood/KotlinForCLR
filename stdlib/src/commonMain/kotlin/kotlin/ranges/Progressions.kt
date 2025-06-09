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

open class CharProgression internal constructor(
	val first: Char,
	endInclusive: Char,
	val step: Int
) : Iterable<Char> {
	init {
		if (step == 0) throw IllegalArgumentException("Step must be non-zero.")
		if (step == Int.MIN_VALUE) throw IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.")
	}

	val last: Char
		external get

	external override fun iterator(): CharIterator
	open external fun isEmpty(): Boolean
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	external override fun toString(): String
	companion object {
		fun fromClosedRange(rangeStart: Char, rangeEnd: Char, step: Int): CharProgression = CharProgression(rangeStart, rangeEnd, step)
	}
}

open class IntProgression internal constructor(
	val first: Int,
	endInclusive: Int,
	val step: Int
) : Iterable<Int> {
	init {
		if (step == 0) throw IllegalArgumentException("Step must be non-zero.")
		if (step == Int.MIN_VALUE) throw IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.")
	}

	val last: Int
		external get

	external override fun iterator(): IntIterator
	open external fun isEmpty(): Boolean
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	external override fun toString(): String
	companion object {
		fun fromClosedRange(rangeStart: Int, rangeEnd: Int, step: Int): IntProgression = IntProgression(rangeStart, rangeEnd, step)
	}
}

open class LongProgression internal constructor(
	val first: Long,
	endInclusive: Long,
	val step: Long
) : Iterable<Long> {
	init {
		if (step == 0L) throw IllegalArgumentException("Step must be non-zero.")
		if (step == Long.MIN_VALUE) throw IllegalArgumentException("Step must be greater than Long.MIN_VALUE to avoid overflow on negation.")
	}

	val last: Long
		external get

	external override fun iterator(): LongIterator
	open external fun isEmpty(): Boolean
	external override fun equals(other: Any?): Boolean
	external override fun hashCode(): Int
	external override fun toString(): String
	companion object {
		fun fromClosedRange(rangeStart: Long, rangeEnd: Long, step: Long): LongProgression = LongProgression(rangeStart, rangeEnd, step)
	}
}