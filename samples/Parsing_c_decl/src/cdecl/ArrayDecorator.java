package cdecl;

public class ArrayDecorator implements Decorator {

	private int size;

	public ArrayDecorator(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "array of " + size;
	}
}
