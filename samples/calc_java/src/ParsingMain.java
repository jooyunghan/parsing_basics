import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

import expr.ExpressionParser;
import expr.MyTokenizer;
import expr.Token;

public class ParsingMain {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String input = "1-2*(3-4.2)";
		myTokenizerTest(input);
		tokenizerTest(input);
		parserTest(input);
	}

	private static void parserTest(String input) throws IOException {
		System.out.println("ExpressionParser --- " + input);
		ExpressionParser ep = new ExpressionParser(input);
		System.out.println(ep.expression());
	}

	private static void tokenizerTest(String input) throws IOException {
		System.out.println("StreamTokenizer --- " + input);
		StreamTokenizer t = new StreamTokenizer(new StringReader(input));
		t.ordinaryChar('-');
		while (t.nextToken() != StreamTokenizer.TT_EOF) {
			System.out.println(t);
		}
	}

	private static void myTokenizerTest(String input) {
		System.out.println("MyTokenizer --- " + input);
		MyTokenizer tokenizer = new MyTokenizer(input);
		while (true) {
			Token t = tokenizer.gettoken();
			if (t.kind == Token.EOF)
				break;
			System.out.println(t);
		}
	}
}
