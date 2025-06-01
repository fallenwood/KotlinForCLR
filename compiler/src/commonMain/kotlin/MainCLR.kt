import compiler.clr.CLRCompiler
import compiler.clr.CLRCompilerArguments
import java.io.File

fun main() {
	val out = File("..\\csharp\\KotlinCLR\\gen")
	out.listFiles()?.forEach { it.delete() }
	val arguments = CLRCompilerArguments().apply {
		freeArgs += "../kotlin/src"
		kotlinHome = "home/clr"
		destination = out.absolutePath
		sdkHome = "C:\\Program Files\\dotnet\\packs\\Microsoft.NETCore.App.Ref\\9.0.5\\ref\\net9.0"
	}
	val compiler = CLRCompiler()
	val exitCode = compiler.exec(System.err, arguments)
	println("exit, code: $exitCode")
}