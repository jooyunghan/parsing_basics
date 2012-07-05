package cdecl;

public class Declaration {
	public Type type;
	public PrimaryDeclarator declarator;

	@Override
	public String toString() {
		return declarator + " " + type;
	}
}
