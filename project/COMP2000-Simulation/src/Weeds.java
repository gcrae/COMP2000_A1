import java.awt.*;

public class Weeds extends Plant {

    public Weeds(Point position, Window window, Sky sky) {
        super(position, window, sky);
        this.setBackground(Color.darkGray);
    }

    @Override
    public void spread() {
        Radius radius = new Radius(position, spreadRadius);
        for (int i = 0; i < spreadNum; i++) {
            Point newPoint = radius.getRandomPoint();
            window.addToGround(new Weeds(newPoint, window, sky), null);
        }
    }

    @Override public void seedAction() {}
    @Override public void seedlingAction() {}
    @Override public void juvenileAction() {}
    @Override public void adultAction() {}
    @Override public void deadAction() {}
}