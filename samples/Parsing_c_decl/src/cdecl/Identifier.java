package cdecl;

public class Identifier implements PrimaryDeclarator {

	private String name;

	public Identifier(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name + " is"; 
	}

}
