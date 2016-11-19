package rasterization;

/**
 * Created by Michal Formanek on 17.11.16.
 */
final public class Point {

	private final double x, y;

	public Point(final double x, final double y) {
		this.x = x;
		this.y = y;
	}


	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}


	@Override
	public String toString() {
		return "Point[" + x + "," + y +
				']';
	}
}
