package rasterfillops.scanline;

import annotations.NotNull;
import geometry.Line;
import rasterdata.RasterImage;
import geometry.Point;
import util.PointConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Michal Formanek on 25.12.16.
 */
public class ScanLiner<P> implements ScanLine<P> {


    @Override
    public RasterImage<P> fill(List<Point> points, RasterImage<P> img, P newValue) {
        final RasterImage[] image = new RasterImage[]{img};
        @NotNull List<Point> convertedPoints = convertPoints(points, img);
        @NotNull List<Line> lines = createLines(convertedPoints);

        final int minY = convertedPoints.stream().mapToInt(point -> (int) point.getY()).min().orElse(0);
        final int maxY = convertedPoints.stream().mapToInt(point -> (int) point.getY()).max().orElse(img.getHeight());

        //TODO
        IntStream.rangeClosed(minY, maxY).forEach(y -> {
            List<Integer> intersections = findIntersections(lines, y);
            for (int i = 0; i < intersections.size() - 1; i = i + 2) {
                image[0] = connectIntersections(intersections.get(i), intersections.get(i + 1), y, newValue, img);
            }
        });

        return image[0];
    }

    private List<Integer> findIntersections(List<Line> lines, final int y) {
        return lines.stream()
                .filter(line -> line.intersects(y))
                .map(line -> (int) line.intersection(y))
                .sorted()
                .collect(Collectors.toList());
    }

    private List<Line> createLines(List<Point> points) {
        return IntStream.range(0, points.size())
                .mapToObj(i -> new Line(points.get(i), points.get((i + 1) % points.size())))
                .collect(Collectors.toList());
    }

    private List<Point> convertPoints(final List<Point> points, final RasterImage<P> img) {
        return points
                .stream()
                .map((p) -> PointConverter.convertFromNDC(p, img.getWidth(), img.getHeight()))
                .map((p) -> new Point((int) p.getX(), (int) p.getY()))
                .collect(Collectors.toList());
    }

    private RasterImage<P> connectIntersections(final int startX, final int endX, final int y, final P newValue, final RasterImage<P> img) {
        return IntStream
                .rangeClosed(startX, endX)
                .mapToObj(i -> img.withPixel(i, y, newValue))
                .reduce((a, b) -> b)
                .orElse(img);
    }
}