package rasterization;

import annotations.NotNull;
import geometry.Point;
import rasterdata.RasterImage;

import java.util.List;

/**
 * Created by Michal Formanek on 17.11.16.
 */
public interface PolygonRasterizer<PixelType> {

	/**
	 * Returns a RasterImage with a rasterized polygon described as list of Points in a right-handed (y axis inverted with respect to raster image row axis) normalized coordinates
	 *
	 * @param img    RasterImage to form background to the polygon, must not be null
	 * @param points List of polygon's vertices normalized to [-1,1], must not be null
	 * @param liner  LineRasterizer to draw edges of Polygon, must not be null
	 * @param pixel  pixel value to store in each line pixel, must not be null
	 * @return RasterImage with the polygon rasterized over the contents of the given image, never null
	 */
	@NotNull
	RasterImage<PixelType> drawPolygon(@NotNull RasterImage<PixelType> img, @NotNull List<Point> points, @NotNull LineRasterizer<PixelType> liner, @NotNull PixelType pixel);
}
