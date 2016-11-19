package util;

/**
 * Created by Michal Formanek on 18.11.16.
 */
public enum Tool {
	LINE("Line"), POLYGON("Polygon"), CIRCLE("Circle"), CLEAR("Clear"), COLOR("Choose color");
	private final String display;

	Tool(String s) {
		display = s;
	}

	@Override
	public String toString() {
		return display;
	}
}
