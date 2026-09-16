import java.awt.*;

public abstract class Flower extends Plant {
    public Flower(Point p, Window window, Sky sky) {
        super(p, window, sky);
    }

    public void bloom() {
        this.setBackground(Color.RED);      
        if ((int) (Math.random() * 100) == 0) {
            spread();
        }
    }  

    @Override
    public void adultAction() {
        bloom();
    }
}