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