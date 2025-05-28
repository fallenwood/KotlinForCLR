import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.compilerRunner.toArgumentStrings
import java.io.File

fun main() {
	val out = File("out")
	out.listFiles()?.forEach { it.delete() }
	val arguments = K2JVMCompilerArguments().apply {
		freeArgs += "../kotlin/src"
		kotlinHome = "home/jvm"
		destination = out.absolutePath
	}
	val compiler = K2JVMCompiler()
	val exitCode = compiler.exec(System.err, *arguments.toArgumentStrings().toTypedArray())
	println("exit, code: $exitCode")
}