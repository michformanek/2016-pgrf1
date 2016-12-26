package rasterfillops.scanline;

import annotations.NotNull;
import rasterdata.RasterImage;
import geometry.Point;

import java.util.List;

/**
 * Created by Michal Formanek on 25.12.16.
 */
public interface ScanLine<P> {

    @NotNull
    RasterImage<P> fill(@NotNull List<Point> points, @NotNull RasterImage<P> img, @NotNull P newValue);

}
