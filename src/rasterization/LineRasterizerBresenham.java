package rasterization;

import annotations.NotNull;
import rasterdata.RasterImage;

/**
 * Created by Michal Formanek on 18.11.16.
 */
public class LineRasterizerBresenham<PixelType> implements LineRasterizer<PixelType>

{
	@Override
	@NotNull
	public RasterImage<PixelType> drawLine(@NotNull final RasterImage<PixelType> img, final double x1, final double y1, final double x2, final double y2, @NotNull PixelType pixel) {
		final int c1 = (int) ((img.getWidth() - 1) * 0.5 * (x1 + 1));
		final int r1 = (int) ((img.getHeight() - 1) * 0.5 * (1 - y1));
		final int c2 = (int) ((img.getWidth() - 1) * 0.5 * (x2 + 1));
		final int r2 = (int) ((img.getHeight() - 1) * 0.5 * (1 - y2));
		int x = c1, y = r1;
		if ((c1 == c2) && (r1 == r2)) {
			img.withPixel(r1, c1, pixel);
		} else {
			int dx = Math.abs(c2 - c1);
			int dy = Math.abs(r2 - r1);
			int diff = dx - dy;

			int pomX, pomY;

			if (c1 < c2)
				pomX = 1;
			else
				pomX = -1;
			if (r1 < r2)
				pomY = 1;
			else
				pomY = -1;

			while ((x != c2) || (y != r2)) {
				int p = 2 * diff;
				if (p > -dy) {
					diff = diff - dy;
					x = x + pomX;
				}
				if (p < dx) {
					diff = diff + dx;
					y = y + pomY;
				}
				img.withPixel(x, y, pixel);
			}
		}
		return img;
	}
}
