package cdecl;
import java.util.List;


public class Declarator implements PrimaryDeclarator {
	public List<Decorator> decorator;
	public PrimaryDeclarator primary;

	@Override
	public String toString() {
		return primary + deco();
	}

	private String deco() {
		StringBuilder sb = new StringBuilder();
		for (Decorator d : decorator) {
			sb.append(" " + d);
		}
		return sb.toString();
	}
}
