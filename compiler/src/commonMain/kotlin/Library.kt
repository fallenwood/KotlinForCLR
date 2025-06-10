import compiler.clr.CLRCompiler
import compiler.clr.CLRCompilerArguments
import java.io.File

fun main() {
	val out = File("out")
	out.listFiles()?.forEach { it.delete() }
	val arguments = CLRCompilerArguments().apply {
		freeArgs += "../stdlib/src"
		destination = out.absolutePath
		noDotnet = true
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