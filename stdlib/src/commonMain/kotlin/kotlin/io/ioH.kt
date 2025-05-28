package kotlin.io

internal class ReadAfterEOFException(message: String?) : RuntimeException(message)
internal interface Serializable