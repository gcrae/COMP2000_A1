import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

public class Radius {
    private final Point center;
    private final int radius;
    private static final Random random = new Random();

    public Radius(Point center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point getRandomPoint() {
        double angle = random.nextDouble() * 2 * Math.PI;
        double r = random.nextDouble() * radius;
        int x = (int) (center.x + r * Math.cos(angle));
        int y = (int) (center.y + r * Math.sin(angle));
        return new Point(x, y);
    }

    public Point randomPointWithin(Rectangle bounds, int plantWidth, int plantHeight) {
        if (bounds == null || bounds.width <= 0 || bounds.height <= 0) {
            return null;
        }

        Point rawPoint = getRandomPoint();

        int minX = bounds.x;
        int maxX = bounds.x + bounds.width - plantWidth;
        int minY = bounds.y;
        int maxY = bounds.y + bounds.height - plantHeight;

        if (maxX < minX) maxX = minX;
        if (maxY < minY) maxY = minY;

        int clampedX = Math.clamp(rawPoint.x, minX, maxX);
        int clampedY = Math.clamp(rawPoint.y, minY, maxY);

        Point clampedPoint = new Point(clampedX, clampedY);

        if (!isWithin(clampedPoint, bounds)) {
            return null;
        }

        return clampedPoint;
    }

    public Point randomNonOverlappingPoint(Rectangle bounds, int plantWidth, int plantHeight, List<Rectangle> existingBounds, int maxAttempts) {
        for (int i = 0; i < maxAttempts; i++) {
            Point candidate = randomPointWithin(bounds, plantWidth, plantHeight);
            if (candidate == null) {
                continue;
            }

            Rectangle candidateRect = new Rectangle(
                candidate.x - plantWidth / 2, 
                candidate.y - plantHeight / 2, 
                plantWidth, 
                plantHeight
            );

            boolean intersects = false;
            if (existingBounds != null) {
                for (Rectangle existing : existingBounds) {
                    if (existing != null && candidateRect.intersects(existing)) {
                        intersects = true;
                        break;
                    }
                }
            }

            if (!intersects) {
                return candidate;
            }
        }
        return null;
    }

    public static boolean isWithin(Point p, Rectangle bounds) {
        if (p == null || bounds == null) return false;
        return bounds.contains(p);
    }
}