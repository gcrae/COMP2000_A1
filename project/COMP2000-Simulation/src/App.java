import java.awt.Point;

public class App {
    public static void main(String[] args) {
        Window window = new Window();
        Controller controller = new Controller(window);

        // Spawn both Sunflower and Weeds at startup so the full ecosystem is active
        Sunflower initialSunflower = new Sunflower(new Point(200, 200), window, window.getSky());
        Weeds initialWeed = new Weeds(new Point(400, 300), window, window.getSky());

        window.addToGround(initialSunflower, null);
        window.addToGround(initialWeed, null);
    }
}