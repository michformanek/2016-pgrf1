package rasterization;

/**
 * Created by Michal Formanek on 18.11.16.
 */
public class LineRasterizerInstantiator<PixelType> {

	/**
	 * Returns new instance of LineRasterizer specified by given type.
	 *
	 * @param type Type of returned LineRasterizer
	 * @return LineRasterizer of given type, LineRasterizerDDA if implementation does not exist
	 */
	public LineRasterizer<PixelType> getLineRasterizer(LineRasterizerType type) {
		if (type == LineRasterizerType.TRIVIAL)
			return new LineRasterizerTrivial<>();
		else if (type == LineRasterizerType.DDA)
			return new LineRasterizerDDA<>();
		else if (type == LineRasterizerType.BRESENHAM)
			return new LineRasterizerBresenham<>();
		else
			return new LineRasterizerDDA<>();
	}
}
