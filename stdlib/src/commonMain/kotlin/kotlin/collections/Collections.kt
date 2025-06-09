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

package kotlin.collections

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
interface Iterable<out T> {
	operator fun iterator(): Iterator<T>
}

@ActualizeByClrBuiltinProvider
interface MutableIterable<out T> : Iterable<T> {
	override fun iterator(): MutableIterator<T>
}

@ActualizeByClrBuiltinProvider
interface Collection<out E> : Iterable<E> {
	val size: Int
	fun isEmpty(): Boolean
	operator fun contains(element: @UnsafeVariance E): Boolean
	override fun iterator(): Iterator<E>
	fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean
}

@ActualizeByClrBuiltinProvider
interface MutableCollection<E> : Collection<E>, MutableIterable<E> {
	override fun iterator(): MutableIterator<E>
	fun add(element: E): Boolean
	fun remove(element: E): Boolean
	fun addAll(elements: Collection<E>): Boolean
	fun removeAll(elements: Collection<E>): Boolean
	fun retainAll(elements: Collection<E>): Boolean
	fun clear()
}

@ActualizeByClrBuiltinProvider
interface List<out E> : Collection<E> {
	override val size: Int
	override fun isEmpty(): Boolean
	override fun contains(element: @UnsafeVariance E): Boolean
	override fun iterator(): Iterator<E>
	override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean
	operator fun get(index: Int): E
	fun indexOf(element: @UnsafeVariance E): Int
	fun lastIndexOf(element: @UnsafeVariance E): Int
	fun listIterator(): ListIterator<E>
	fun listIterator(index: Int): ListIterator<E>
	fun subList(fromIndex: Int, toIndex: Int): List<E>
}

@ActualizeByClrBuiltinProvider
interface MutableList<E> : List<E>, MutableCollection<E> {
	override fun add(element: E): Boolean
	override fun remove(element: E): Boolean
	override fun addAll(elements: Collection<E>): Boolean
	fun addAll(index: Int, elements: Collection<E>): Boolean
	override fun removeAll(elements: Collection<E>): Boolean
	override fun retainAll(elements: Collection<E>): Boolean
	override fun clear()
	operator fun set(index: Int, element: E): E
	fun add(index: Int, element: E)
	fun removeAt(index: Int): E
	override fun listIterator(): MutableListIterator<E>
	override fun listIterator(index: Int): MutableListIterator<E>
	override fun subList(fromIndex: Int, toIndex: Int): MutableList<E>
}

@ActualizeByClrBuiltinProvider
interface Set<out E> : Collection<E> {
	override val size: Int
	override fun isEmpty(): Boolean
	override fun contains(element: @UnsafeVariance E): Boolean
	override fun iterator(): Iterator<E>
	override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean
}

@ActualizeByClrBuiltinProvider
interface MutableSet<E> : Set<E>, MutableCollection<E> {
	override fun iterator(): MutableIterator<E>
	override fun add(element: E): Boolean
	override fun remove(element: E): Boolean
	override fun addAll(elements: Collection<E>): Boolean
	override fun removeAll(elements: Collection<E>): Boolean
	override fun retainAll(elements: Collection<E>): Boolean
	override fun clear()
}

@ActualizeByClrBuiltinProvider
interface Map<K, out V> {
	val size: Int
	fun isEmpty(): Boolean
	fun containsKey(key: K): Boolean
	fun containsValue(value: @UnsafeVariance V): Boolean
	operator fun get(key: K): V?
	fun getOrDefault(key: K, defaultValue: @UnsafeVariance V): V {
		throw NotImplementedError()
	}
	val keys: Set<K>
	val values: Collection<V>
	val entries: Set<Entry<K, V>>

	interface Entry<out K, out V> {
		val key: K
		val value: V
	}
}

@ActualizeByClrBuiltinProvider
interface MutableMap<K, V> : Map<K, V> {
	fun put(key: K, value: V): V?
	fun remove(key: K): V?
	fun remove(key: K, value: V): Boolean {
		return true
	}
	fun putAll(from: Map<out K, V>)
	fun clear()
	override val keys: MutableSet<K>
	override val values: MutableCollection<V>
	override val entries: MutableSet<MutableEntry<K, V>>

	interface MutableEntry<K, V> : Map.Entry<K, V> {
		fun setValue(newValue: V): V
	}
}