package expr;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;


/*
exression = sum
sum = product (('+'|'-') product)*
product = term (('*'|'/') term)*
term =  number | '(' expression ')'
 */
public class ExpressionParser {
	private StreamTokenizer st;

	public ExpressionParser(String string) {
		st = new StreamTokenizer(new StringReader(string));
		st.ordinaryChar('-');
	}

	// expression = sum
	public double expression() throws IOException {
		return sum();
	}
	
	// sum = product (('+'|'-') product)*
	double sum() throws IOException {
		double result = product();
		while (true) {
			st.nextToken();
			if (st.ttype == '+') {
				result += product();
			} else if (st.ttype == '-') {
				result -= product();
			} else {
				st.pushBack();
				break;
			}
		}
		return result;
	}
	
	// product = term (('*'|'/') term)*
	double product() throws IOException {
		double result = term();
		while (true) {
			st.nextToken();
			if (st.ttype == '*') {
				result *= term();
			} else if (st.ttype == '/') {
				result /= term();
			} else {
				st.pushBack();
				break;
			}
		}
		return result;
	}	
	
	// term = number | '(' expression ')'
	double term() throws IOException {
		int ttype = st.nextToken();
		if (ttype == StreamTokenizer.TT_NUMBER) {
			return st.nval;
		} else if (ttype == '(') {
			double result = expression();
			st.nextToken();
			return result;
		} else {
			throw new RuntimeException("Number or ( expected");
		}
	}
}