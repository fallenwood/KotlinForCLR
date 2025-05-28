import compiler.clr.CLRCompiler
import compiler.clr.CLRCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.compilerRunner.toArgumentStrings
import java.io.File

fun main() {
	val out = File("out")
	out.listFiles()?.forEach { it.delete() }
	when (false) {
		true -> {
			val arguments = K2JVMCompilerArguments().apply {
				freeArgs += "../stdlib/src"
				destination = out.absolutePath
				noJdk = true
				noStdlib = true
				noReflect = true
				allowKotlinPackage = true
				stdlibCompilation = true
				expectActualClasses = true
				expectBuiltinsAsPartOfStdlib = true
				dontWarnOnErrorSuppression = true
				multiPlatform = true
				commonSources = arrayOf("../stdlib/src")
			}
			val compiler = K2JVMCompiler()
			compiler.exec(System.err, *arguments.toArgumentStrings().toTypedArray())
		}

		false -> {
			val arguments = CLRCompilerArguments().apply {
				freeArgs += "../stdlib/src"
				destination = out.absolutePath
				noSdk = true
				noStdlib = true
				allowKotlinPackage = true
				stdlibCompilation = true
				expectActualClasses = true
				dontWarnOnErrorSuppression = true
				multiPlatform = true
				commonSources = arrayOf("../stdlib/src")
			}
			val compiler = CLRCompiler()
			compiler.exec(System.err, arguments)
		}
	}
}