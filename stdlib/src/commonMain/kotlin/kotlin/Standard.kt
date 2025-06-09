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

class NotImplementedError(message: String = "An operation is not implemented.") : Error(message)

inline fun TODO(): Nothing = throw NotImplementedError()
inline fun TODO(reason: String): Nothing = throw NotImplementedError("An operation is not implemented: $reason")

/*
public inline fun <R> run(block: () -> R): R {
	*/
/*contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}*//*

	return block()
}

public inline fun <T, R> T.run(block: T.() -> R): R {
	*/
/*contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}*//*

	return block()
}

public inline fun <T, R> with(receiver: T, block: T.() -> R): R {
	*/
/*contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}*//*

	return receiver.block()
}

public inline fun <T> T.apply(block: T.() -> Unit): T {
	*/
/*contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}*//*

	block()
	return this
}

public inline fun <T> T.also(block: (T) -> Unit): T {
	*/
/*contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}*//*

	block(this)
	return this
}

public inline fun <T, R> T.let(block: (T) -> R): R {
	*/
/*contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}*//*

	return block(this)
}

public inline fun <T> T.takeIf(predicate: (T) -> Boolean): T? {
	*/
/*contract {
		callsInPlace(predicate, InvocationKind.EXACTLY_ONCE)
	}*//*

	return if (predicate(this)) this else null
}

public inline fun <T> T.takeUnless(predicate: (T) -> Boolean): T? {
	*/
/*contract {
		callsInPlace(predicate, InvocationKind.EXACTLY_ONCE)
	}*//*

	return if (!predicate(this)) this else null
}

public inline fun repeat(times: Int, action: (Int) -> Unit) {
//	contract { callsInPlace(action) }

	for (index in 0 until times) {
		action(index)
	}
}*/
