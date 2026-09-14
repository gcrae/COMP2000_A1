
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

//This class will control the major actions taken in the simulation

public class Controller {
    private Window window;

    public Controller (Window window){
        this.window= window;

        Timer skyTimer = new Timer(10000, new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e){
                window.getSky().progressTime();
            }
        });
        skyTimer.start();
    }
    public ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {

        }
    };
}
