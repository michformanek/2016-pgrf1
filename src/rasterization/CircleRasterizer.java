package rasterization;

import annotations.NotNull;
import rasterdata.RasterImage;

import java.util.List;

/**
 * Created by Michal Formanek on 17.11.16.
 */
public interface CircleRasterizer<PixelType> {
	/**
	 * Returns a raster image with a rasterized circle in a right-handed (y axis inverted with respect to raster image row axis) normalized coordinate system
	 *
	 * @param img      raster image to form background to the circle, must not be null
	 * @param center   center of circle, normalized to [-1,1], must not be null
	 * @param arcStart starting point of circle segment, distance between center and this point becomes radius of circle. normalized to [-1,1], must not be null
	 * @param arcEnd   starting point of circle segment, normalized to [-1,1], can be null
	 * @return raster image with the line rasterized over the contents of the given image, never null
	 */
	@NotNull
	RasterImage<PixelType> drawCircle(@NotNull RasterImage<PixelType> img, @NotNull Point center, @NotNull Point arcStart, @NotNull Point arcEnd, @NotNull PixelType pixel);
}
