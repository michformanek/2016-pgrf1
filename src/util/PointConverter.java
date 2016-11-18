package util;

import rasterization.Point;

/**
 * Created by Michal Formanek on 18.11.16.
 */
public class PointConverter {

	/**
	 * Converts Point from device coordinates to NDC
	 *
	 * @param point  Point to be converted, must not be null
	 * @param width  Width of source device in pixels
	 * @param height Height of source device in pixels
	 * @return New Point with converted coordinates
	 */
	public static Point convertToNDC(final Point point, double width, double height) {
		return new Point(point.getX() / width * 2 - 1, point.getY() / height * -2 + 1);
	}

	/**
	 * Converts Point from NDC to device coordinates
	 *
	 * @param point  Point to be converted, must not be null
	 * @param width  Width of target device in pixels
	 * @param height Height of target device in pixels
	 * @return New Point with converted coordinates
	 */
	public static Point convertFromNDC(final Point point, int width, int height) {
		return new Point((width - 1) * 0.5 * (point.getX() + 1), (height - 1) * 0.5 * (1 - point.getY()));
	}
}
