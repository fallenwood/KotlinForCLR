package kotlin.reflect

interface KType {
	val classifier: KClassifier?
	val arguments: List<KTypeProjection>
	val isMarkedNullable: Boolean
}