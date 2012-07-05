package parser;


public class ParserBase {
	public ParserBase(String decl) {
		input = decl;
		pos = 0;
		length = input.length();
	}
	
	protected String input;
	protected int pos;
	protected int length;
	
	protected int parseNumber() {
		Token t = getToken();
		return t.value;
	}

	protected void backup(Token t) {
		pos -= t.text.length();
	}
	
	protected Token getToken() {
		skipSpaces();
		if (pos >= length) {
			return new Token(Token.EOF, "");
		}
		if (Character.isJavaIdentifierStart(input.charAt(pos))) {
			int start = pos;
			pos++;
			while (pos < length && Character.isJavaIdentifierPart(input.charAt(pos))) {
				pos++;
			}
			return new Token(Token.TEXT, input.substring(start, pos));
		}
		
		if (Character.isDigit(input.charAt(pos))) {
			int start = pos;
			pos++;
			while (pos < length && Character.isDigit(input.charAt(pos))) {
				pos++;
			}
			return new Token(Token.NUMBER, input.substring(start, pos));			
		}
		
		Token t = new Token(input.charAt(pos), input.charAt(pos) + "");
		pos++;
		return t;
	}
	
	public void skipSpaces() {
		while (pos < length && Character.isWhitespace(input.charAt(pos))) {
			pos++;
		}
	}
	
	public String remainder() {
		return input.substring(pos);
	}
}