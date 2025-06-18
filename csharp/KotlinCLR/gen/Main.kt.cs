[global::kotlin.clr.KotlinFileClass]
public static class MainKt
{
    public static void main()
    {
        global::System.Collections.Generic.IReadOnlyList items = global::kotlin.collections.CollectionsKt.listOf(        /*
        Unsupported expression: IrVarargImpl
        at IrExpression.visit
        */);
        global::kotlin.io.ConsoleKt.println(items);
    }

    public static void Main(global::System.String[] args)
    {
        global::MainKt.main();
    }
}