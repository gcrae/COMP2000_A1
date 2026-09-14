import java.awt.*;

public class App {
    public static void main(String[] args) {
        Window window = new Window();
        Controller controller = new Controller(window);// Starts controller and weather timer
        int delay = 50; //Refresh 20 times per second   (huh??? twenty? how? it's initialised to fifty, did you mean 40? - Allie) 
        //TODO: needs refresh rate code here or in controller - Allie
        //nm did it in controller, ignore the above - Allie


        new Sunflower(new Point(200, 200), window);
    }
}
