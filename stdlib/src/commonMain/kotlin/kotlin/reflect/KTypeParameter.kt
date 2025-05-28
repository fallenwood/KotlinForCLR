package kotlin.reflect

interface KTypeParameter : KClassifier {
	val name: String
	val upperBounds: List<KType>
	val variance: KVariance
	val isReified: Boolean
}