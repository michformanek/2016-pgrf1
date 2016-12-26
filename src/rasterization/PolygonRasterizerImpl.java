package rasterization;

import annotations.NotNull;
import geometry.Point;
import rasterdata.RasterImage;

import java.util.List;

/**
 * Created by Michal Formanek on 17.11.16.
 */
public class PolygonRasterizerImpl<PixelType> implements PolygonRasterizer<PixelType> {

	@Override
	@NotNull
	public RasterImage<PixelType> drawPolygon(@NotNull final RasterImage<PixelType> img, @NotNull final List<Point> vertices,
	                                          @NotNull final LineRasterizer<PixelType> liner, @NotNull final PixelType pixel) {
		RasterImage<PixelType> result = img;
		for (int i = 0; i < vertices.size(); i++) {
			final double startX = vertices.get(i).getX();
			final double startY = vertices.get(i).getY();
			final double endX = vertices.get((i + 1) % vertices.size()).getX();
			final double endY = vertices.get((i + 1) % vertices.size()).getY();
			result = liner.drawLine(result, startX, startY, endX, endY, pixel);
		}
		return result;
	}
}
