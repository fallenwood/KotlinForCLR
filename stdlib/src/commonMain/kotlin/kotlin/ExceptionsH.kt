package kotlin

open class Error : Throwable {
    constructor()
    constructor(message: String?)
    constructor(message: String?, cause: Throwable?)
    constructor(cause: Throwable?)
}

open class Exception : Throwable {
    constructor()
    constructor(message: String?)
    constructor(message: String?, cause: Throwable?)
    constructor(cause: Throwable?)
}

open class RuntimeException : Exception {
    constructor()
    constructor(message: String?)
    constructor(message: String?, cause: Throwable?)
    constructor(cause: Throwable?)
}

open class IllegalArgumentException : RuntimeException {
    constructor()
    constructor(message: String?)
    constructor(message: String?, cause: Throwable?)
    constructor(cause: Throwable?)
}

open class IllegalStateException : RuntimeException {
    constructor()
    constructor(message: String?)
    constructor(message: String?, cause: Throwable?)
    constructor(cause: Throwable?)
}

open class IndexOutOfBoundsException : RuntimeException {
    constructor()
    constructor(message: String?)
}

open class ConcurrentModificationException : RuntimeException {
    constructor()
    constructor(message: String?)
    constructor(message: String?, cause: Throwable?)
    constructor(cause: Throwable?)
}

open class UnsupportedOperationException : RuntimeException {
    constructor()
    constructor(message: String?)
    constructor(message: String?, cause: Throwable?)
    constructor(cause: Throwable?)
}

open class NumberFormatException : IllegalArgumentException {
    constructor()
    constructor(message: String?)
}

open class NullPointerException : RuntimeException {
    constructor()
    constructor(message: String?)
}

open class ClassCastException : RuntimeException {
    constructor()
    constructor(message: String?)
}

open class AssertionError : Error {
    constructor()
    constructor(message: Any?)
    constructor(message: String?, cause: Throwable?)
}

open class NoSuchElementException : RuntimeException {
    constructor()
    constructor(message: String?)
}

open class ArithmeticException : RuntimeException {
    constructor()
    constructor(message: String?)
}

open class NoWhenBranchMatchedException : RuntimeException {
    constructor()
    constructor(message: String?)
    constructor(message: String?, cause: Throwable?)
    constructor(cause: Throwable?)
}

class UninitializedPropertyAccessException : RuntimeException {
    constructor()
    constructor(message: String?)
    constructor(message: String?, cause: Throwable?)
    constructor(cause: Throwable?)
}

internal class KotlinNothingValueException : RuntimeException {
	constructor() : super()
	constructor(message: String?) : super(message)
	constructor(message: String?, cause: Throwable?) : super(message, cause)
	constructor(cause: Throwable?) : super(cause)
}