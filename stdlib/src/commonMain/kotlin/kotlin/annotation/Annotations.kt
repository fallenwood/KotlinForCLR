package kotlin.annotation

enum class AnnotationTarget {
	CLASS,
	ANNOTATION_CLASS,
	TYPE_PARAMETER,
	PROPERTY,
	FIELD,
	LOCAL_VARIABLE,
	VALUE_PARAMETER,
	CONSTRUCTOR,
	FUNCTION,
	PROPERTY_GETTER,
	PROPERTY_SETTER,
	TYPE,
	EXPRESSION,
	FILE,
	TYPEALIAS
}

enum class AnnotationRetention {
	SOURCE,
	BINARY,
	RUNTIME
}


@Target(AnnotationTarget.ANNOTATION_CLASS)
@MustBeDocumented
annotation class Target(vararg val allowedTargets: AnnotationTarget)

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Retention(val value: AnnotationRetention = AnnotationRetention.RUNTIME)

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Repeatable

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class MustBeDocumented