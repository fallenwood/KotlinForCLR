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
interface Iterator<out T> {
    operator fun next(): T
    operator fun hasNext(): Boolean
}

@ActualizeByClrBuiltinProvider
interface MutableIterator<out T> : Iterator<T> {
    fun remove()
}

@ActualizeByClrBuiltinProvider
interface ListIterator<out T> : Iterator<T> {
    override fun next(): T
    override fun hasNext(): Boolean
    fun hasPrevious(): Boolean
    fun previous(): T
    fun nextIndex(): Int
    fun previousIndex(): Int
}

@ActualizeByClrBuiltinProvider
interface MutableListIterator<T> : ListIterator<T>, MutableIterator<T> {
    override fun next(): T
    override fun hasNext(): Boolean
    override fun remove()
	fun set(element: T)
	fun add(element: T)
}