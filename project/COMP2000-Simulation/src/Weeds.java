import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class Weeds extends Plant {
    private static int liveCount = 0;
    private static final int MAX_POPULATION = 12;

    public Weeds(Point position, Window window, Sky sky) {
        super(position, window, sky);
        liveCount++;
        this.setBackground(Color.darkGray);
    }

    @Override
    protected int getMaxPopulation() {
        return MAX_POPULATION;
    }

    @Override
    protected boolean isAtCapacity() {
        return liveCount >= MAX_POPULATION;
    }

    @Override
    protected void onRemoved() {
        if (liveCount > 0) {
            liveCount--;
        }
    }

    @Override
    protected double stageDurationMultiplier() {
        return 0.6;
    }

    @Override
    protected double environmentSensitivity(double environmentMultiplier) {
        return Math.max(1.0, environmentMultiplier);
    }

    @Override
    protected void juvenileAction() {
        if ((int) (Math.random() * 300) == 0) {
            spread();
        }
    }

    @Override
    protected void adultAction() {
        if ((int) (Math.random() * 300) == 0) {
            spread();
        }
    }

    @Override
    protected void deadAction() {
        this.setBackground(new Color(110, 70, 20));
    }

@Override
public void spread() {
    if (isAtCapacity()) return;
    if (window == null || window.getGround() == null) return;

    Rectangle bounds = new Rectangle(0, 0, window.getGround().getWidth(), window.getGround().getHeight());
    if (bounds.width <= 0 || bounds.height <= 0) return;

    Radius radius = new Radius(position, spreadRadius);
    int seedSize = size / 4; // 15x15 seed dimension

    for (int i = 0; i < spreadNum; i++) {
        if (isAtCapacity()) break;
        Point newPoint = radius.randomNonOverlappingPoint(bounds, seedSize, seedSize, window.getOccupiedBounds(), 30);
        if (newPoint != null) {
            window.addToGround(new Weeds(newPoint, window, sky), null);
        }
    }

    window.getGround().revalidate();
    window.getGround().repaint();
}
}