import java.awt.Color;
import java.awt.Point;

public class Sunflower extends Flower {

    public Sunflower(Point position, Window window, Sky sky) {
        super(position, window, sky);
    }

    @Override
    public void bloom() {
        this.setBackground(Color.YELLOW);
        if ((int) (Math.random() * 100) == 0) {
            spread();
        }
    }

    @Override public void seedAction() {}
    @Override public void seedlingAction() {}
    @Override public void juvenileAction() {}
    @Override public void deadAction() {}

    @Override
    public void spread() {
        Radius radius = new Radius(position, spreadRadius);
        for (int i = 0; i < spreadNum; i++) {
            Point newPoint = radius.getRandomPoint();
            window.addToGround(new Sunflower(newPoint, window, sky), null);
        }
    }
}
