import java.awt.Color;
import java.awt.Point;
import java.util.TimerTask;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public abstract class Plant extends JPanel implements Growable {
    protected final Sky sky;

    private int growthState = LifeStage.SEED;
    private double growthProgress = 0.0;

    private static final long GROWTH_INTERVAL_MS = 5000L;
    private static final double MS_PER_STAGE = 5000.0;

    protected int size = 60;
    protected Point position;
    protected int spreadNum;
    protected int spreadRadius;
    protected Window window;

    private java.util.Timer tickTimer;
    private java.util.Timer growTimer;
    private boolean removalScheduled = false;

    public Plant(Point p, Window window, Sky sky) {
        this.position = p;
        this.window = window;
        this.sky = sky;
        this.spreadNum = 2;
        this.spreadRadius = 100;

        this.setBounds(p.x - size / 8, p.y - size / 8, size / 4, size / 4);
        this.setBackground(Color.darkGray);

        startTimers();
    }

    private void startTimers() {
        tickTimer = new java.util.Timer(true);
        tickTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    currentBehaviour();
                    repaint();
                });
            }
        }, 0, 25);

        growTimer = new java.util.Timer(true);
        growTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> advanceGrowth());
            }
        }, GROWTH_INTERVAL_MS, GROWTH_INTERVAL_MS);
    }

    private void stopTimers() {
        if (tickTimer != null) {
            tickTimer.cancel();
            tickTimer = null;
        }
        if (growTimer != null) {
            growTimer.cancel();
            growTimer = null;
        }
    }

    private void advanceGrowth() {
        if (LifeStage.isDead(growthState)) {
            stopTimers();
            scheduleRemoval();
            return;
        }

        double rawMultiplier = 1.0;
        try {
            rawMultiplier = sky.getGrowthMultiplier();
        } catch (InvalidWeatherException e) {
            e.printStackTrace();
        }

        double effectiveMultiplier = environmentSensitivity(rawMultiplier);
        double effectiveMsPerStage = MS_PER_STAGE / stageDurationMultiplier();

        growthProgress += (GROWTH_INTERVAL_MS * effectiveMultiplier) / effectiveMsPerStage;
        if (growthProgress >= 1.0) {
            growthProgress = 0.0;
            advanceStage();
        }

        currentBehaviour();
        repaint();
    }

    private void advanceStage() {
        if (growthState < LifeStage.DEAD) {
            growthState++;
            LifeStage.validate(growthState);
            if (LifeStage.isDead(growthState)) {
                stopTimers();
            }
        }
    }

    public boolean isDead() {
        return LifeStage.isDead(growthState);
    }

    private void currentBehaviour() {
        switch (growthState) {
            case LifeStage.SEED -> {
                this.setBackground(new Color(79, 46, 9));
                seedAction();
            }
            case LifeStage.SEEDLING -> {
                this.setBackground(new Color(2, 184, 9));
                seedlingAction();
            }
            case LifeStage.JUVENILE -> {
                this.setBounds(position.x - size / 4, position.y - size / 4, size / 2, size / 2);
                this.setBackground(new Color(1, 120, 5));
                juvenileAction();
            }
            case LifeStage.ADULT -> {
                this.setBounds(position.x - size / 2, position.y - size / 2, size, size);
                this.setBackground(new Color(1, 71, 4));
                adultAction();
            }
            case LifeStage.DEAD -> {
                this.setBackground(Color.BLACK);
                deadAction();
                scheduleRemoval();
            }
            default -> throw new IllegalStateException("Unrecognized lifecycle stage: " + growthState);
        }
    }

    private void scheduleRemoval() {
        if (removalScheduled) return;
        removalScheduled = true;

        stopTimers();

        javax.swing.Timer removalTimer = new javax.swing.Timer(3000, e -> {
            if (window != null) {
                window.removeFromGround(this);
                window.refresh();
            }
            onRemoved();
        });
        removalTimer.setRepeats(false);
        removalTimer.start();
    }

    @Override
    public void grow() {
        advanceStage();
    }

    protected double stageDurationMultiplier() {
        return 1.0;
    }

    protected double environmentSensitivity(double environmentMultiplier) {
        return environmentMultiplier;
    }

    protected abstract int getMaxPopulation();
    protected abstract boolean isAtCapacity();
    protected abstract void onRemoved();

    protected void seedAction() {}
    protected void seedlingAction() {}
    protected void juvenileAction() {}
    protected void adultAction() {}
    protected void deadAction() {}

    public abstract void spread();

    protected double getGrowthProgress() {
        return growthProgress;
    }

    protected int getGrowthState() {
        return growthState;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Plant at " + position + " [Stage: " + growthState + ", Progress: " + String.format("%.2f", growthProgress) + "]";
    }
}