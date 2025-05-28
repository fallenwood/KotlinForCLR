package kotlin.reflect

interface KClass<T : Any> : KClassifier {
	val simpleName: String?
	val qualifiedName: String?
	fun isInstance(value: Any?): Boolean
	override fun equals(other: Any?): Boolean
	override fun hashCode(): Int
}