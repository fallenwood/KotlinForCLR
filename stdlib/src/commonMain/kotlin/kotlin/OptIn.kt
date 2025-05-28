package kotlin

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.EXPRESSION
import kotlin.annotation.AnnotationTarget.FILE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.LOCAL_VARIABLE
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.annotation.AnnotationTarget.TYPEALIAS
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER
import kotlin.reflect.KClass

@Target(ANNOTATION_CLASS)
@Retention(BINARY)
annotation class RequiresOptIn(
	val message: String = "",
	val level: Level = Level.ERROR
) {
	enum class Level {
		WARNING,
		ERROR,
	}
}

@Target(
	CLASS, PROPERTY, LOCAL_VARIABLE, VALUE_PARAMETER, CONSTRUCTOR, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER, EXPRESSION, FILE, TYPEALIAS
)
@Retention(SOURCE)
annotation class OptIn(
	vararg val markerClass: KClass<out Annotation>
)

@Target(CLASS)
@Retention(BINARY)
@RequiresOptIn
annotation class ExperimentalSubclassOptIn

@Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
@Target(CLASS)
@Retention(BINARY)
annotation class SubclassOptInRequired(
	vararg val markerClass: KClass<out Annotation>,
)