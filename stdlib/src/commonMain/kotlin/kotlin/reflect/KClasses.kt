package kotlin.reflect

external fun <T : Any> KClass<T>.cast(value: Any?): T
internal val KClass<*>.qualifiedOrSimpleName: String?
	external get

external fun <T : Any> KClass<T>.safeCast(value: Any?): T?