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