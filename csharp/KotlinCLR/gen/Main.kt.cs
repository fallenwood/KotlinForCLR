public sealed class MainKt
{
    public static void main()
    {
        global::kotlin.io.ConsoleKt.println("Enter any word: ");
        global::System.String yourWord = global::kotlin.io.ConsoleKt.readln();
        global::kotlin.io.ConsoleKt.print("You entered the word: ");
        global::kotlin.io.ConsoleKt.print(yourWord);
    }
    public static void Main(global::System.String[] args)
    {
        global::MainKt.main();
    }
}