import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

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

    private Timer tickTimer;
    private Timer growTimer;

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
        tickTimer = new Timer(true);
        tickTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    currentBehaviour();
                    repaint();
                });
            }
        }, 0, 25);

        growTimer = new Timer(true);
        growTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> advanceGrowth());
            }
        }, GROWTH_INTERVAL_MS, GROWTH_INTERVAL_MS);
    }

    private void advanceGrowth() {
        if (LifeStage.isDead(growthState)) return;

        double environmentMultiplier = 1.0;
        try {
            environmentMultiplier = sky.getGrowthMultiplier();
        } catch (InvalidWeatherException e) {
            e.printStackTrace();
        }

        growthProgress += (GROWTH_INTERVAL_MS * environmentMultiplier) / MS_PER_STAGE;
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
            }
            default -> throw new IllegalStateException("Unrecognized lifecycle stage: " + growthState);
        }
    }

    @Override
    public void grow() {
        advanceStage();
    }

    protected double getGrowthProgress() {
        return growthProgress;
    }

    protected int getGrowthState() {
        return growthState;
    }

    public abstract void seedAction();
    public abstract void seedlingAction();
    public abstract void juvenileAction();
    public abstract void adultAction();
    public abstract void deadAction();

    public Point getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Plant at " + position + " [Stage: " + growthState + ", Progress: " + String.format("%.2f", growthProgress) + "]";
    }
}