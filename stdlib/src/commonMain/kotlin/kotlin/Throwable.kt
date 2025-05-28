package kotlin

import kotlin.internal.ActualizeByClrBuiltinProvider

@ActualizeByClrBuiltinProvider
open class Throwable(
	open val message: String?,
	open val cause: Throwable?
) {
	constructor(message: String?) : this(message, null)
	constructor(cause: Throwable?) : this(cause?.toString(), cause)
	constructor() : this(null, null)
}