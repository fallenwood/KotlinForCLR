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