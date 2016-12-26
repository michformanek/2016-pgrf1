package rasterization;

import annotations.NotNull;
import annotations.Nullable;
import geometry.Point;
import rasterdata.RasterImage;
import util.PointConverter;

/**
 * Created by mformanek on 20.11.16.
 */
public class CircleRasterizerImpl<PixelType> implements CircleRasterizer<PixelType> {
	@Override
	public RasterImage<PixelType> drawCircle(@NotNull RasterImage<PixelType> img, @NotNull Point center, @NotNull Point arcStart, @Nullable Point arcEnd, @NotNull final LineRasterizer<PixelType> liner, @NotNull PixelType pixel) {
		RasterImage<PixelType> result = img;

		final Point c = PointConverter.convertFromNDC(center, img.getWidth(), img.getHeight());
		final Point s = PointConverter.convertFromNDC(arcStart, img.getWidth(), img.getHeight());

		final int radius = (int) Math.abs(Math.sqrt((Math.pow((s.getX() - c.getX()), 2)) + (Math.pow((s.getY() - c.getY()), 2))));
		final int x0 = (int) c.getX();
		final int y0 = (int) c.getY();
		int x = radius;
		int y = 0;
		int err = 0;

		while (x >= y) {
			result = result.withPixel(x0 + x, y0 + y, pixel);
			result = result.withPixel(x0 + y, y0 + x, pixel);
			result = result.withPixel(x0 - y, y0 + x, pixel);
			result = result.withPixel(x0 - x, y0 + y, pixel);
			result = result.withPixel(x0 - x, y0 - y, pixel);
			result = result.withPixel(x0 - y, y0 - x, pixel);
			result = result.withPixel(x0 + y, y0 - x, pixel);
			result = result.withPixel(x0 + x, y0 - y, pixel);

			y += 1;
			err += 1 + 2 * y;
			if (2 * (err - x) + 1 > 0) {
				x -= 1;
				err += 1 - 2 * x;
			}
		}
		return result;
	}
}
