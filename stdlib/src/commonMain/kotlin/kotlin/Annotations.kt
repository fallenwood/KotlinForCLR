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

package kotlin

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.EXPRESSION
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FILE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.LOCAL_VARIABLE
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.annotation.AnnotationTarget.TYPEALIAS
import kotlin.annotation.AnnotationTarget.TYPE_PARAMETER
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

@Target(CLASS, FUNCTION, PROPERTY, ANNOTATION_CLASS, CONSTRUCTOR, PROPERTY_SETTER, PROPERTY_GETTER, TYPEALIAS)
@MustBeDocumented
annotation class Deprecated(
	val message: String,
	val replaceWith: ReplaceWith = ReplaceWith(""),
	val level: DeprecationLevel = DeprecationLevel.WARNING
)

@Target(CLASS, FUNCTION, PROPERTY, ANNOTATION_CLASS, CONSTRUCTOR, PROPERTY_SETTER, PROPERTY_GETTER, TYPEALIAS)
@MustBeDocumented
annotation class DeprecatedSinceKotlin(
	val warningSince: String = "",
	val errorSince: String = "",
	val hiddenSince: String = ""
)

@Target()
@Retention(BINARY)
@MustBeDocumented
annotation class ReplaceWith(
	val expression: String,
	vararg val imports: String
)

enum class DeprecationLevel {
	WARNING,
	ERROR,
	HIDDEN
}

@Target(TYPE)
@MustBeDocumented
annotation class ExtensionFunctionType

@Target(TYPE)
@MustBeDocumented
annotation class ContextFunctionTypeParams(val count: Int)

@Target(TYPE)
@MustBeDocumented
annotation class ParameterName(val name: String)

@Target(CLASS, ANNOTATION_CLASS, TYPE_PARAMETER, PROPERTY, FIELD, LOCAL_VARIABLE, VALUE_PARAMETER,
	CONSTRUCTOR, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER, TYPE, EXPRESSION, FILE, TYPEALIAS)
@Retention(SOURCE)
annotation class Suppress(vararg val names: String)

@Target(TYPE)
@Retention(SOURCE)
@MustBeDocumented
annotation class UnsafeVariance

@Target(CLASS, PROPERTY, FIELD, CONSTRUCTOR, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER, TYPEALIAS)
@Retention(BINARY)
@MustBeDocumented
annotation class SinceKotlin(val version: String)

@Target(ANNOTATION_CLASS)
@Retention(BINARY)
@MustBeDocumented
annotation class DslMarker

@Target(CLASS, CONSTRUCTOR, FUNCTION, PROPERTY)
@Retention(BINARY)
@MustBeDocumented
annotation class PublishedApi