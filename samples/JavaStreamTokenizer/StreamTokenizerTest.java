import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

/*
 * 
 */
public class StreamTokenizerTest {
	public static void main(String[] args) throws IOException {
		calc();
		cdecl();
	}
	
	public static void cdecl() throws IOException {
		Reader r = new StringReader("int (*a[3])()");
		StreamTokenizer streamTokenizer = new StreamTokenizer(r);
		printAll(streamTokenizer);
	}

	public static void calc() throws IOException {
		Reader r = new StringReader("1.23+3 * (12 -3) ");
		StreamTokenizer streamTokenizer = new StreamTokenizer(r);
		streamTokenizer.ordinaryChars('-', '-'); // '-' '3' 으로 분리
		streamTokenizer.ordinaryChars('/', '/');
		streamTokenizer.slashStarComments(false);
		
		printAll(streamTokenizer);
	}

	public static void printAll(StreamTokenizer streamTokenizer)
			throws IOException {
		while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF ) {
			switch (streamTokenizer.ttype) {
			default:
				System.out.println("Unknown: [" + (char)streamTokenizer.ttype + "]");
				break;
			case StreamTokenizer.TT_EOL:
				System.out.println("EOL"); 
				break;
			case StreamTokenizer.TT_NUMBER: 
				System.out.println("Number: [" + streamTokenizer.nval +"]");
				break;
			case StreamTokenizer.TT_WORD: 
				System.out.println("Word: [" + streamTokenizer.sval +"]"); 
				break;
			case '\"': 
				System.out.println("Double Quoted String: [" + streamTokenizer.sval + "]"); 
				break;
			case '\'': 
				System.out.println("Single Quoted String: [" + streamTokenizer.sval + "]"); 
				break;
			}
		}
		System.out.println("EOF");
	}
}
