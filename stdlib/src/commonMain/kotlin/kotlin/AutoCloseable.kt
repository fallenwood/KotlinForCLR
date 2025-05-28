package kotlin

interface AutoCloseable {
	fun close()
}

inline fun AutoCloseable(crossinline closeAction: () -> Unit): AutoCloseable = throw Throwable()

inline fun <T : AutoCloseable?, R> T.use(block: (T) -> R): R = throw Throwable()