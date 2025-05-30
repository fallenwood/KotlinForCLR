using kotlin.clr;

namespace kotlin;

[KotlinObject]
public sealed class Unit {
	public static readonly Unit INSTANCE = new();

	private Unit() { }

	public override string ToString() => "kotlin.Unit";
}