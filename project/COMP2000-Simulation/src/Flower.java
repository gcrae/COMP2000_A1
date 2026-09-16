import java.awt.*;

public abstract class Flower extends Plant {
    public Flower(Point p, Window window, Sky sky) {
        super(p, window, sky);
    }

    public void bloom() {
        this.setBackground(Color.RED);
    }

    @Override
    protected void adultAction() {
        bloom();
        if ((int) (Math.random() * 100) == 0) {
            spread();
        }
    }
}