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