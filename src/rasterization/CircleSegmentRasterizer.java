package rasterization;

import annotations.NotNull;
import annotations.Nullable;
import rasterdata.RasterImage;
import util.PointConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mformanek on 20.11.16.
 */
public class CircleSegmentRasterizer<PixelType> implements CircleRasterizer<PixelType> {
	@Override
	public RasterImage<PixelType> drawCircle(@NotNull RasterImage<PixelType> img, @NotNull Point center, @NotNull Point arcStart, @Nullable Point arcEnd, @NotNull LineRasterizer<PixelType> liner, @NotNull PixelType pixel) {
		RasterImage<PixelType> result = img;

		final List<Point> pointList = new ArrayList<>();
		final Point c = PointConverter.convertFromNDC(center, img.getWidth(), img.getHeight());
		final Point s = PointConverter.convertFromNDC(arcStart, img.getWidth(), img.getHeight());
		final Point e = (arcEnd == null) ? s : PointConverter.convertFromNDC(arcEnd, img.getWidth(), img.getHeight());
		final int radius = (int) Math.abs(Math.sqrt((Math.pow((s.getX() - c.getX()), 2)) + (Math.pow((s.getY() - c.getY()), 2))));
		final double start = Math.atan2(s.getX() - c.getX(), s.getY() - c.getY());
		double end = Math.atan2(e.getX() - c.getX(), e.getY() - c.getY());

		double angle = start;
		if (start > end) {
			end += 2 * Math.PI;
		}

		while (angle <= end) {
			double x = c.getX() + radius * Math.sin(angle);
			double y = c.getY() + radius * Math.cos(angle);
			pointList.add(PointConverter.convertToNDC(new Point(x, y), img.getWidth(), img.getHeight()));
			angle += 0.01;
		}
		for (int i = 0; i < pointList.size() - 1; i++) {
			result = liner.drawLine(result, pointList.get(i).getX(), pointList.get(i).getY(), pointList.get(i + 1).getX(), pointList.get(i + 1).getY(), pixel);
		}
		result = liner.drawLine(result, pointList.get(0).getX(), pointList.get(0).getY(), center.getX(), center.getY(), pixel);
		result = liner.drawLine(result, pointList.get(pointList.size() - 1).getX(), pointList.get(pointList.size() - 1).getY(), center.getX(), center.getY(), pixel);

		return result;
	}
}
