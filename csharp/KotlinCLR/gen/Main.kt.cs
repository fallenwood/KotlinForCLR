public sealed class MainKt
{
    public static global::System.Double PI { get;  } 
    public static global::System.Int32 x { get; set;  } 
    public static void incrementX()
    {
        global::MainKt.x = global::MainKt.x + 1;
    }
    public static void main()
    {
        global::kotlin.io.ConsoleKt.println("x = " + global::MainKt.x + ", PI = " + global::MainKt.PI);
        global::MainKt.incrementX();
        global::kotlin.io.ConsoleKt.println("x = " + global::MainKt.x + ", PI = " + global::MainKt.PI);
    }
    public static void Main(global::System.String[] args)
    {
        global::MainKt.main();
    }
}