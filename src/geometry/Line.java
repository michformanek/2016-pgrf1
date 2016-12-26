package geometry;

import annotations.NotNull;

/**
 * Created by Michal Formanek on 25.12.16.
 */
public class Line {
    private final
    @NotNull
    Point startPoint, endPoint;

    public Line(final @NotNull Point startPoint, final @NotNull Point endPoint) {
        if (endPoint.getY() > startPoint.getY()) {
            this.endPoint = endPoint;
            this.startPoint = startPoint;
        } else {
            this.startPoint = endPoint;
            this.endPoint = startPoint;
        }
    }

    public double getK() {
        return (endPoint.getX() - startPoint.getX()) / (endPoint.getY() - startPoint.getY());
    }

    public double getQ() {
        return startPoint.getX() - getK() * startPoint.getY();
    }

    public double getStartPointX() {
        return startPoint.getX();
    }

    public double getEndPointX() {
        return endPoint.getX();
    }

    public double getStartPointY() {
        return startPoint.getY();
    }

    public double getEndPointY() {
        return endPoint.getY();
    }

    public boolean intersects(double y) {
        return ((startPoint.getY() <= y) && (y < endPoint.getY()));
    }

    public double intersection(double y) {
        return (int) (getK() * y + getQ());
    }
}
