import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Window extends JFrame {
    public static int WIN_WIDTH = 800;
    public static int WIN_HEIGHT = 600;
    private final Sky sky;
    private final Ground ground;

    public Window() {
        sky = new Sky();
        ground = new Ground();

        this.setTitle("GAASK Plant Simulation COMP2000");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;

        this.add(sky, c);
        this.add(ground, c);

        this.pack();
        this.setVisible(true);
    }

    public void addToGround(Component comp, Object constraints) {
        ground.add(comp, constraints);
    }

    public void removeFromGround(Component comp) {
        ground.remove(comp);
    }

    public void refresh() {
        ground.revalidate();
        ground.repaint();
    }

    public Sky getSky() {
        return this.sky;
    }

    public Ground getGround() {
        return this.ground;
    }

    public List<Rectangle> getOccupiedBounds() {
        List<Rectangle> result = new ArrayList<>();
        for (Component c : ground.getComponents()) {
            if (c instanceof Plant) {
                result.add(c.getBounds());
            }
        }
        return result;
    }
}