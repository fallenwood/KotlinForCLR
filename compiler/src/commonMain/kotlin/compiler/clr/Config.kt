package compiler.clr

import org.jetbrains.kotlin.config.CompilerConfigurationKey
import java.io.File

object CLRConfigurationKeys {
	val OUTPUT_DIRECTORY = CompilerConfigurationKey.create<File>("output directory")
	val SDK_HOME = CompilerConfigurationKey.create<File>("sdk home")
	val NO_SDK = CompilerConfigurationKey.create<Boolean>("no sdk")
}