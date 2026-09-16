import java.awt.Point;

public class App {
    public static void main(String[] args) {
        Window window = new Window();
        
        // Initialize the controller to start the simulation clock
        Controller controller = new Controller(window);

        // FIX: Pass window.getSky() as the third argument to match the new constructor
        Sunflower initialPlant = new Sunflower(new Point(200, 200), window, window.getSky());
        
        window.addToGround(initialPlant, null);
    }
}