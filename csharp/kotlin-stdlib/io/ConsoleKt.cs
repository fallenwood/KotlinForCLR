using kotlin.clr;

namespace kotlin.io;

[ClrFileClass]
public sealed class ConsoleKt {
	public static void print(object? message) => Console.Write(message);
	public static void print(int message) => Console.Write(message);
	public static void print(long message) => Console.Write(message);
	public static void print(byte message) => Console.Write(message);
	public static void print(short message) => Console.Write(message);
	public static void print(char message) => Console.Write(message);
	public static void print(bool message) => Console.Write(message);
	public static void print(float message) => Console.Write(message);
	public static void print(double message) => Console.Write(message);
	public static void print(char[] message) => Console.Write(message);
	public static void println(object? message) => Console.WriteLine(message);
	public static void println(int message) => Console.WriteLine(message);
	public static void println(long message) => Console.WriteLine(message);
	public static void println(byte message) => Console.WriteLine(message);
	public static void println(short message) => Console.WriteLine(message);
	public static void println(char message) => Console.WriteLine(message);
	public static void println(bool message) => Console.WriteLine(message);
	public static void println(float message) => Console.WriteLine(message);
	public static void println(double message) => Console.WriteLine(message);
	public static void println(char[] message) => Console.WriteLine(message);
	public static void println() => Console.WriteLine();
	public static string readln() => readlnOrNull();
	public static string? readlnOrNull() => readLine();
	public static string? readLine() => Console.ReadLine();
}