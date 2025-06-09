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

interface KProperty<out V> : KCallable<V>
interface KMutableProperty<V> : KProperty<V>

interface KProperty0<out V> : KProperty<V>, () -> V {
	fun get(): V
}

interface KMutableProperty0<V> : KProperty0<V>, KMutableProperty<V> {
	fun set(value: V)
}

interface KProperty1<T, out V> : KProperty<V>, (T) -> V {
	fun get(receiver: T): V
}

interface KMutableProperty1<T, V> : KProperty1<T, V>, KMutableProperty<V> {
	fun set(receiver: T, value: V)
}

interface KProperty2<D, E, out V> : KProperty<V>, (D, E) -> V {
	fun get(receiver1: D, receiver2: E): V
}

interface KMutableProperty2<D, E, V> : KProperty2<D, E, V>, KMutableProperty<V> {
	fun set(receiver1: D, receiver2: E, value: V)
}