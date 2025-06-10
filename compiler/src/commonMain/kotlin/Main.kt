import compiler.clr.CLRCompiler
import compiler.clr.CLRCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.parseCommandLineArguments

fun main(args: Array<String>) {
	val compiler = CLRCompiler()
	val exitCode = compiler.exec(System.err, parseCommandLineArguments<CLRCompilerArguments>(args.toList()))
	println("exit, code: $exitCode")
}