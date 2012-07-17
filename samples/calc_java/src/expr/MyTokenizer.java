package expr;

// number, (, ), +, ...
public class MyTokenizer {
	private String input;
	private int pos;

	public MyTokenizer(String string) {
		this.input = string;
		this.pos = 0;
	}

	public Token gettoken() {
		// skip white spaces
		while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
			pos++;
		}
		
		if (pos == input.length())
			return new Token(Token.EOF);
		
		// number = [0-9] [0-9.]* 
		if (Character.isDigit(input.charAt(pos))) {
			int start = pos;
			while (pos < input.length() 
					&& (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
				pos++;
			}
			return new Token(Token.NUMBER, Double.parseDouble(input.substring(start, pos)));
		}
		
		// ordinary character as a token
		return new Token(input.charAt(pos++));
	}
	
}