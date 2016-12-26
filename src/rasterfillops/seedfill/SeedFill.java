package rasterfillops.seedfill;

import annotations.NotNull;
import annotations.Nullable;
import rasterdata.RasterImage;
import rasterfillops.patterns.Pattern;

import java.util.function.Predicate;

/**
 * Created by Michal Formanek on 25.12.16.
 */
public interface SeedFill<P> {

    @NotNull
    RasterImage<P> fill(@NotNull RasterImage<P> img, int c, int r,
                        @NotNull P newValue, @NotNull Predicate<P> isInArea);

    @NotNull
    RasterImage<P> fillPattern(@NotNull RasterImage<P> img, int c, int r,
                               @NotNull P newValue, @NotNull P newBack, @NotNull Predicate<P> isInArea,
                               @Nullable Pattern pattern);


}
