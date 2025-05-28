package compiler.clr.frontend

import org.jetbrains.kotlin.platform.SimplePlatform
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.platform.TargetPlatformVersion

abstract class ClrPlatform : SimplePlatform("CLR") {
	override val oldFashionedDescription: String
		get() = "CLR "
}

class JvmPlatformImpl : ClrPlatform() {
	override fun toString(): String = platformName

	override val oldFashionedDescription: String
		get() = "JVM"

	override val targetPlatformVersion: TargetPlatformVersion
		get() = TargetPlatformVersion.NoVersion

	override fun equals(other: Any?): Boolean = other is JvmPlatformImpl
	override fun hashCode(): Int = JvmPlatformImpl::class.hashCode()
}

object ClrPlatforms {
	private val UNSPECIFIED_SIMPLE_CLR_PLATFORM = JvmPlatformImpl()

	val unspecifiedClrPlatform: TargetPlatform
		get() = CompatClrPlatform

	object CompatClrPlatform : TargetPlatform(setOf(UNSPECIFIED_SIMPLE_CLR_PLATFORM))
}