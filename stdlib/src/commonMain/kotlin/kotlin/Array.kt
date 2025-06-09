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
class Array<T> {
	@Suppress("WRONG_MODIFIER_TARGET")
	inline constructor(size: Int, init: (Int) -> Double)
	external operator fun get(index: Int): T
	external operator fun set(index: Int, value: T)
	@Suppress("MUST_BE_INITIALIZED_OR_BE_ABSTRACT")
	val size: Int
	external operator fun iterator(): Iterator<T>
}
