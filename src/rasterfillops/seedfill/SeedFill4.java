package rasterfillops.seedfill;

import java.util.Optional;
import java.util.function.Predicate;

import annotations.NotNull;
import rasterdata.RasterImage;
import rasterfillops.patterns.Pattern;
import rasterfillops.patterns.SolidPattern;

public class SeedFill4<P> implements SeedFill<P> {

    @Override
    public RasterImage<P> fill(RasterImage<P> img, int c, int r, P newValue, Predicate<P> isInArea) {
        SolidPattern pattern = new SolidPattern();
        return fillPattern(img, c, r, newValue, newValue, isInArea, pattern);
    }

    @Override
    public
    @NotNull
    RasterImage<P> fillPattern(final @NotNull RasterImage<P> img,
                               final int c,
                               final int r,
                               final @NotNull P newValue,
                               final @NotNull P newBack,
                               final @NotNull Predicate<P> isInArea,
                               final @NotNull Pattern pattern) {
        return new Object() {
            @NotNull
            RasterImage<P> fill(final @NotNull RasterImage<P> img,
                                final int c, final int r) {
                return img.getPixel(c, r)
                        .flatMap((final @NotNull P pixel) -> {
                            if (isInArea.test(pixel)) { // pixel se ma vyplnit
                                return Optional.of(
                                        fill(
                                                fill(
                                                        fill(
                                                                fill(
                                                                        (pattern.filled(r, c)) ? img.withPixel(c, r, newValue) : img.withPixel(c, r, newBack),
                                                                        c, r - 1),
                                                                c + 1, r),
                                                        c, r + 1),
                                                c - 1, r)
                                );
                            }
                            return Optional.empty();
                        })
                        .orElse(img);
            }
        }.fill(img, c, r);


    }
}
