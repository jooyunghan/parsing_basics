package parser;

public class Token {

	public static final char TEXT = '7';
	public static final char NUMBER = '8';
	public static final char EOF = '9';
	public String text;
	public char kind;
	public int value;

	public Token(char kind, String s) {
		this.kind = kind;
		this.text = s;
		if (kind == NUMBER) {
			this.value = Integer.parseInt(s);
		}
	}
	

}
