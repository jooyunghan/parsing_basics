import java.util.ArrayList;
import java.util.List;

import cdecl.*;
import parser.*;

/*
declaration = type-specifier declarator
declarator = pointer suffixed
pointer = ¡®*¡¯ *
suffixed =  primary suffix
primary = id / '(' declarator ')¡®
suffix = (¡®[¡¯ number ¡®]¡¯ / ¡®(¡¯ ¡®)¡¯)*

 */
public class CDeclParser extends ParserBase {
	public CDeclParser(String decl) {
		super(decl);
	}

	public static void main(String[] args) {
		CDeclParser p = new CDeclParser("char ***a[1]");
		System.out.println(p.parseDeclaration());
		System.out.println(p.remainder());
	}
	
	// declaration = type-specifier declarator
	public Declaration parseDeclaration() {
		Declaration d = new Declaration();
		d.type = parseTypeSpecifier();
		d.declarator = parseDeclarator();
		return d;
	}

	// declarator = pointer suffixed
	public Declarator parseDeclarator() {
		Declarator decl = new Declarator();
		decl.decorator = parsePointer();
		decl.primary = parseSuffixed();
		return decl;
	}

	// suffixed = primary suffix
	private PrimaryDeclarator parseSuffixed() {
		Declarator decl = new Declarator();
		decl.primary = parsePrimary();		
		decl.decorator = parseSuffix();
		return decl;
	}

	// suffix = ( '[' number ']' / '(' ... ')' ) *
	private List<Decorator> parseSuffix() {
		List<Decorator> deco = new ArrayList<Decorator>();
		while (true) {
			Token t = getToken();
			if (t.kind == '[') {
				deco.add(new ArrayDecorator(parseNumber()));
				getToken(); // should be "]"
			} else if  (t.kind == '(') {
				deco.add(new FunctionDecorator());
				getToken(); // should be ")"
			} else {
				backup(t);
				break;
			}
		}
		return deco;
	}


	// primary = identifier / '(' declarator ')'
	private PrimaryDeclarator parsePrimary() {
		Token t = getToken();
		if (t.kind == Token.TEXT) {
			return new Identifier(t.text);
		} else {
			Declarator d = parseDeclarator();
			getToken(); // should be ')'
			return d;
		}
	}

	// pointer = '*' *
	private List<Decorator> parsePointer() {
		List<Decorator> deco = new ArrayList<Decorator>();
		Token t = getToken();
		while (t.kind == '*') {
			deco.add(new PointerDecorator());
			t = getToken();
		}
		backup(t);
		return deco;
	}

	// type-specifier = identifier
	public Type parseTypeSpecifier() {
		return new Type(getToken().text);
	}
}
