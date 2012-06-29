import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* print word which starts with q or Q but not followed by u */

public class Main {
	public static void main(String[] args) throws IOException {
		Pattern p = Pattern.compile("\\b(Q|q)(?!u).*?\\b");
		// anchor : word boundary \b
		// *?     : reluctant 
		// (?! )  : negative lookahead
		Matcher m = p.matcher("quick qee quite uniqee");
		while (m.find()) {
			System.out.println(m.group());
		}
	}
}

