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