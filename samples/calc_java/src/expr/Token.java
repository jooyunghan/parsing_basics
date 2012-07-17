package expr;

public class Token {
	public static final int NUMBER = 0;
	public static final int EOF = -1;

	public int kind;
	public double value;
	
	public Token(int type) {
		this.kind = type;
	}
	public Token(int type, double n) {
		this.kind = type;
		if (type == NUMBER) {
			value = n;
		}
	}
	
	@Override
	public String toString() {
		if (kind == NUMBER) {
			return "Token[n=" + value + "]";
		} else if (kind == EOF) {
			return "EOF";
		} else 
			return "Token[\'" + (char)kind + "\']";
	}	
}