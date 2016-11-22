package rasterization;

import annotations.NotNull;
import annotations.Nullable;
import rasterdata.RasterImage;
import util.PointConverter;

/**
 * Created by mformanek on 20.11.16.
 */
public class CircleSegmentRasterizer<PixelType> implements CircleRasterizer<PixelType> {
	@Override
	public RasterImage<PixelType> drawCircle(@NotNull RasterImage<PixelType> img, @NotNull Point center, @NotNull Point arcStart, @NotNull Point arcEnd, @NotNull PixelType pixel) {
		RasterImage<PixelType> result = img;

		final Point c = PointConverter.convertFromNDC(center, img.getWidth(), img.getHeight());
		final Point s = PointConverter.convertFromNDC(arcStart, img.getWidth(), img.getHeight());
		final Point e = (arcEnd == null) ? s : PointConverter.convertFromNDC(arcEnd, img.getWidth(), img.getHeight());

		final int radius = (int) Math.abs(Math.sqrt((Math.pow((s.getX() - c.getX()), 2)) + (Math.pow((s.getY() - c.getY()), 2))));
		final double angle = calculateAngle(s.getX(), s.getY(), e.getX(), e.getY());
		int x;
		int y;

		for (int i = 0; i < angle; i++) {
			double angleTemp = 2 * Math.PI * i / angle;
			x = (int) (c.getX() + radius * Math.cos(angleTemp));
			y = (int) (c.getY() + radius * Math.sin(angleTemp));
			result = result.withPixel(x, y, pixel);
		}

		return result;
	}

	private double calculateAngle(double beginX, double beginY, double endX, double endY) {
		double angle = Math.toDegrees(Math.atan2(endY - beginY, endX - beginX));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

}
