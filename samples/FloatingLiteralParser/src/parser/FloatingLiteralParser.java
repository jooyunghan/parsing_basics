package parser;
/**
	Recognizes floating-literal.
	
	According to the following grammar(PEG)
	
floating-literal <-
	fractional-constant exponent-part?  (f/l/F/L)?

fractional-constant <-
      (digit-sequence? '.' digit-sequence ) 
	/ (digit-sequence '.')

exponent-part <- 
	('e' / 'E')  sign? digit-sequence

sign <-
	'+' / '-'

digit-sequence <- 
	digit+

digit <- 
	[0-9]

 */
public class FloatingLiteralParser {
	String input;
	int    pos;
	
	public FloatingLiteralParser(String string) {
		input = string;
		pos = 0;
	}

	public String remain() {
		return input.substring(pos);
	}
	
	private boolean eof() {
		return pos >= input.length();
	}	
	
	// floating-literal = fractional-constant exponent-part?  (f/l/F/L)?
	public boolean floatingLiteral() {
		int old = pos;
		if (fractionalConstant()) {
			exponentPart();
			if (!eof() && (input.charAt(pos) == 'f' || input.charAt(pos) == 'F' || input.charAt(pos) == 'l' || input.charAt(pos) == 'L')) {
				pos++;
				return true;
			}
			return true;
		}
		pos = old;
		return false;
	}

	/*
	fractional-constant = (digit-sequence? '.' digit-sequence ) 
	                    / (digit-sequence '.')
	 */
	private boolean fractionalConstant() {
		int old = pos;
		digitSequence();
		if (!eof() && input.charAt(pos) == '.') {
			pos++;
			if (digitSequence()) {
				return true;
			}
		}
		pos = old;
		if (digitSequence()) {
			if (!eof() && input.charAt(pos) == '.') {
				pos++;
				return true;
			}
		}
		pos = old;
		return false;
	}
	
	// exponent-part <-  ('e' / 'E')  sign? digit-sequence
	private boolean exponentPart() {
		int old = pos;
		if (!eof() && input.charAt(pos) == 'e' || input.charAt(pos) == 'E') {
			pos++;
			sign();
			if (digitSequence()) {
				return true;
			}
		}
		pos = old;
		return false;
	}

	// '+' / '-'
	private boolean sign() {
		if (!eof() && (input.charAt(pos) == '+' || input.charAt(pos) == '-')) {
			pos++;
			return true;
		}
		return false;
	}

	// digit+
	private boolean digitSequence() {
		if (digit()) {
			while (digit()) 
				;
			return true;
		}
		return false;
	}
	
	// 0-9
	private boolean digit() {
		if (!eof() && input.charAt(pos) >= '0' && input.charAt(pos) <= '9') {
			pos++;
			return true;
		}
		return false;
	}

}
