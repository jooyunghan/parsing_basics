import parser.FloatingLiteralParser;

public class Main {
	public static void main(String[] args) {
		FloatingLiteralParser parser = new FloatingLiteralParser("12.123E-12f");
		System.out.println(parser.floatingLiteral()); // true
		System.out.println(parser.remain()); // "abc"
	}
}
