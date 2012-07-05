import java.util.ArrayList;

import cdecl.*;


public class TestCDecl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// int *a
		Declaration d = new Declaration() {{
			type = new Type("int");
			declarator = new Declarator() {{
				decorator = new ArrayList<Decorator>() {{
					add(new PointerDecorator());
				}};
				primary = new Identifier("a");
			}};
		}};
		System.out.println(d);
	}

}
