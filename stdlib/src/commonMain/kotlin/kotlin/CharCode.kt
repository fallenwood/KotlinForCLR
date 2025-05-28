package kotlin

inline fun Char(code: Int): Char = throw Throwable()
//external fun Char(code: UShort): Char
inline val Char.code: Int get() = this.toInt()