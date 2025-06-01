public sealed class Rectangle
{
    public Rectangle(global::System.Double height, global::System.Double length) : base()
    {
        this.height = height;
        this.length = length;
        this.perimeter = ((this.height) + (this.length)) * (2);
    }
    public global::System.Double height { get; }
    public global::System.Double length { get; }
    public global::System.Double perimeter { get; }
}
public sealed class MainKt
{
    public static void main()
    {
        global::Rectangle rectangle = new global::Rectangle(5.0, 2.0);
        global::kotlin.io.ConsoleKt.println("The perimeter is " + rectangle.perimeter);
    }
    public static void Main(global::System.String[] args)
    {
        global::MainKt.main();
    }
}