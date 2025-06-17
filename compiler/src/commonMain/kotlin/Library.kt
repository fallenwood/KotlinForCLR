import compiler.clr.CLRCompiler
import compiler.clr.CLRCompilerArguments

fun main() {
	val arguments = CLRCompilerArguments().apply {
		freeArgs += "../stdlib/src"
		destination = "out"
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