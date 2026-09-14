import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

/*
 Manages environmental rendering, time-of-day progression, and dynamic weather patterns
 */
public class Sky extends JPanel {

    // Time of day constants[cite: 11]
    public static final int DAWN = 0;
    public static final int DAY = 1;
    public static final int DUSK = 2;
    public static final int NIGHT = 3;

    // Weather state constants
    public static final int SUNNY = 0;
    public static final int CLOUDY = 1;
    public static final int OVERCAST = 2;
    public static final int RAINY = 3;

    private int timeState;
    private int weatherState;
    private final Random random;

    // Type-safe generic list tracking weather transition history
    private final List<String> weatherLog;

    public Sky() {
        this.timeState = DAWN;
        this.weatherState = SUNNY;
        this.random = new Random();
        this.weatherLog = new ArrayList<>(); //generic instantiation

        this.setPreferredSize(new Dimension(Window.WIN_WIDTH, Window.WIN_HEIGHT / 4)); // Proportional bounds[cite: 11, 14]
        logWeatherChange("INITIALIZED: DAWN, SUNNY");
    }

    /*  
    Simulation Logic & Exception Handling
    */

    /*
     Advances time by one phase (DAWN -> DAY -> DUSK -> NIGHT)
     Includes a random chance to trigger a weather shift.
     */
    public void progressTime() {
        this.timeState = (this.timeState + 1) % 4;

        //probability of weather shift on time change
        if (random.nextInt(100) < 35) {
            randomizeWeather();
        }

        repaint();
    }

    /*
    Updates weather using weighted random probabilities and logs the transition.
     */
    public void randomizeWeather() {
        int chance = random.nextInt(100);
        if (chance < 40) {
            this.weatherState = SUNNY;
        } else if (chance < 70) {
            this.weatherState = CLOUDY;
        } else if (chance < 85) {
            this.weatherState = OVERCAST;
        } else {
            this.weatherState = RAINY;
        }

        logWeatherChange("RANDOMIZED: Weather state code " + this.weatherState);
        repaint();
    }

    /*
    Explicitly sets weather state.
    throws InvalidWeatherException If the provided code is outside [0..3].
     */
    public void changeWeather(int newWeather) throws InvalidWeatherException {
        // EXCEPTION HANDLING
        if (newWeather < SUNNY || newWeather > RAINY) {
            throw new InvalidWeatherException("Weather code " + newWeather + " is invalid. Must be between 0 and 3.");
        }

        this.weatherState = newWeather;
        logWeatherChange("MANUAL CHANGE: Weather set to " + newWeather);
        repaint();
    }

    private void logWeatherChange(String logEntry) {
        weatherLog.add(logEntry);
    }

    // GENERICS
    public List<String> getWeatherLog() {
        return new ArrayList<>(weatherLog);
    }

    // Environmental API (Queried by Plants)

    /*
    Calculates a combined environmental growth multiplier based on light and moisture.
    
    return multiplier (> 1.0 accelerates growth, < 1.0 slows growth).
     */
    public double getGrowthMultiplier() {
        double timeFactor = switch (timeState) {
            case DAY -> 1.5;
            case DAWN -> 1.0;
            case DUSK -> 0.6;
            case NIGHT -> 0.2; // Reduced photosynthesis at night
            default -> 1.0;
        };

        double weatherFactor = switch (weatherState) {
            case SUNNY -> 1.2;
            case CLOUDY -> 0.9;
            case OVERCAST -> 0.7;
            case RAINY -> 1.4; // Rain supplies soil moisture
            default -> 1.0;
        };

        return timeFactor * weatherFactor;
    }

    public boolean isRaining() {
        return this.weatherState == RAINY;
    }

    public boolean isShaded() {
        return this.weatherState == OVERCAST || this.timeState == NIGHT;
    }

    public int getTimeState() {
        return this.timeState;
    }

    public int getWeatherState() {
        return this.weatherState;
    }

    // Standard JRE Graphics2D Rendering

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Anti-aliasing for smooth circles and lines
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // 1. Calculate & Fill Sky Background
        Color skyColor = getSkyColor();
        g2d.setColor(skyColor);
        g2d.fillRect(0, 0, width, height);

        // 2. Draw Sun or Moon
        drawCelestialBody(g2d, width);

        // 3. Draw Weather Effects (Clouds / Rain)
        if (weatherState != SUNNY) {
            drawClouds(g2d);
        }
        if (weatherState == RAINY) {
            drawRain(g2d, width, height);
        }
    }

    private Color getSkyColor() {
        Color baseColor = switch (timeState) {
            case DAWN -> new Color(228, 151, 89);  // Warm orange
            case DAY -> new Color(135, 206, 235);   // Sky blue
            case DUSK -> new Color(38, 83, 141);   // Blue-Gray
            case NIGHT -> new Color(20, 25, 50);    // Dark navy
            default -> new Color(135, 206, 235);
        };

        // Darken sky depending on cloud density
        if (weatherState == OVERCAST || weatherState == RAINY) {
            return darkenColor(baseColor, 0.45f);
        } else if (weatherState == CLOUDY) {
            return darkenColor(baseColor, 0.18f);
        }
        return baseColor;
    }

    private Color darkenColor(Color color, float factor) {
        int r = Math.max((int) (color.getRed() * (1.0f - factor)), 0);
        int g = Math.max((int) (color.getGreen() * (1.0f - factor)), 0);
        int b = Math.max((int) (color.getBlue() * (1.0f - factor)), 0);
        return new Color(r, g, b);
    }

    private void drawCelestialBody(Graphics2D g2d, int width) {
        int size = 50;
        int x = width - 90;
        int y = 20;

        if (timeState == DAY || timeState == DAWN) {
            g2d.setColor(new Color(255, 220, 50)); // Yellow Sun
            g2d.fillOval(x, y, size, size);
        } else {
            g2d.setColor(new Color(235, 235, 210)); // Off-white Moon
            g2d.fillOval(x, y, size - 5, size - 5);
        }
    }

    private void drawClouds(Graphics2D g2d) {
        // Dark grey clouds for rain, translucent white for fair clouds
        g2d.setColor(weatherState == RAINY ? new Color(90, 95, 110) : new Color(245, 245, 245, 210));

        g2d.fillOval(60, 30, 90, 40);
        g2d.fillOval(100, 15, 80, 45);
        g2d.fillOval(290, 35, 110, 45);
        g2d.fillOval(340, 20, 90, 50);
        g2d.fillOval(560, 25, 100, 40);
    }

    private void drawRain(Graphics2D g2d, int width, int height) {
        g2d.setColor(new Color(160, 200, 255, 180)); // Semi-transparent rain streaks
        for (int x = 15; x < width; x += 28) {
            int startY = random.nextInt(height / 2);
            g2d.drawLine(x, startY, x - 7, startY + 20);
        }
    }
}