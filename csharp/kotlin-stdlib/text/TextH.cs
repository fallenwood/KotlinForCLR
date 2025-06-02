using kotlin.clr;

namespace kotlin.text;

[KotlinFileClass]
public class TextH {
	[KotlinExtension]
	public static string replace(string receiver, string oldValue, string newValue, bool ignoreCase = false) {
		return receiver.Replace(oldValue, newValue, ignoreCase, null);
	}
}