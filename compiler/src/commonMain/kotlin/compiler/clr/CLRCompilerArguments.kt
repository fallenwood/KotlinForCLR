package compiler.clr

import org.jetbrains.kotlin.cli.common.arguments.Argument
import org.jetbrains.kotlin.cli.common.arguments.CommonCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.DefaultValue
import org.jetbrains.kotlin.cli.common.arguments.GradleInputTypes
import org.jetbrains.kotlin.cli.common.arguments.GradleOption

class CLRCompilerArguments : CommonCompilerArguments() {
	@Argument(
		value = "-d",
		valueDescription = "<directory>",
		description = "Destination directory for generated CLR assemblies."
	)
	var destination: String? = null
		set(value) {
			checkFrozen()
			field = if (value.isNullOrEmpty()) null else value
		}

	@Argument(
		value = "-assembly",
		shortName = "-asm",
		valueDescription = "<path>",
		description = "List of directories and JAR/ZIP archives to search for user class files."
	)
	var assembly: String? = null
		set(value) {
			checkFrozen()
			field = if (value.isNullOrEmpty()) null else value
		}

	@Argument(
		value = "-sdk-home",
		valueDescription = "<path>",
		description = "Include a custom JDK from the specified location in the classpath instead of the default 'JAVA_HOME'."
	)
	var sdkHome: String? = null
		set(value) {
			checkFrozen()
			field = if (value.isNullOrEmpty()) null else value
		}

	@GradleOption(
		value = DefaultValue.BOOLEAN_FALSE_DEFAULT,
		gradleInputType = GradleInputTypes.INPUT,
		shouldGenerateDeprecatedKotlinOptions = true,
	)
	@Argument(value = "-no-sdk", description = "Don't automatically include the Java runtime in the classpath.")
	var noSdk = false
		set(value) {
			checkFrozen()
			field = value
		}

	@Argument(
		value = "-no-stdlib",
		description = "Don't automatically include the Kotlin/JVM stdlib and Kotlin reflection dependencies in the classpath."
	)
	var noStdlib = false
		set(value) {
			checkFrozen()
			field = value
		}
}