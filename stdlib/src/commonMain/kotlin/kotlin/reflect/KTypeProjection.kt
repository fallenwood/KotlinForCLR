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

package kotlin.reflect

data class KTypeProjection(
	val variance: KVariance?,
	val type: KType?
) {
	external override fun toString(): String
	companion object {
		@PublishedApi
		internal val star: KTypeProjection = KTypeProjection(null, null)

		val STAR: KTypeProjection get() = star

		external fun invariant(type: KType): KTypeProjection
		external fun contravariant(type: KType): KTypeProjection
		external fun covariant(type: KType): KTypeProjection
	}
}