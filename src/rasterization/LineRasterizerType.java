package rasterization;

/**
 * Created by Michal Formanek on 18.11.16.
 */
public enum LineRasterizerType {
	TRIVIAL("Trivialni algorimus"), DDA("DDA"), BRESENHAM("Bresenham");
	private final String display;

	LineRasterizerType(String s) {
		display = s;
	}

	@Override
	public String toString() {
		return display;
	}


}
