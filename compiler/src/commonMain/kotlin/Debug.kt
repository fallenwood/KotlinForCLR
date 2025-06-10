import compiler.clr.CLRCompiler
import compiler.clr.CLRCompilerArguments
import java.io.File

fun main() {
	val out = File("..\\csharp\\KotlinCLR\\gen")
	out.listFiles()?.forEach { it.delete() }
	val arguments = CLRCompilerArguments().apply {
		freeArgs += "../kotlin/src"
		destination = out.absolutePath
		kotlinHome = "../home"
		dotnetHome = "C:\\Program Files\\dotnet\\packs\\Microsoft.NETCore.App.Ref\\9.0.5\\ref\\net9.0"
		dotnetVersion = "9.0.5"
	}
	val compiler = CLRCompiler()
	val exitCode = compiler.exec(System.err, arguments)
	println("exit, code: $exitCode")
}