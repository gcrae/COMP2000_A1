import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

/*
 Manages environmental rendering, time-of-day progression, and dynamic weather patterns
 */
public class Sky extends JPanel {

    private int timeState;
    private int weatherState;
    private final Random random;

    // Type-safe generic list tracking weather transition history
    private final List<String> weatherLog;

    public Sky() {
        this.timeState = TimeOfDay.DAWN;
        this.weatherState = Weather.SUNNY;
        this.random = new Random();
        this.weatherLog = new ArrayList<>(); 

        this.setPreferredSize(new Dimension(Window.WIN_WIDTH, Window.WIN_HEIGHT / 4));
        logWeatherChange("INITIALIZED: DAWN, SUNNY");
    }

    /*  
    Simulation Logic & Exception Handling
    */

    public void progressTime() {
        this.timeState = (this.timeState + 1) % 4;

        if (random.nextInt(100) < 35) {
            randomizeWeather();
        }

        repaint();
    }

    public void randomizeWeather() {
        int chance = random.nextInt(100);
        if (chance < 40) {
            this.weatherState = Weather.SUNNY;
        } else if (chance < 70) {
            this.weatherState = Weather.CLOUDY;
        } else if (chance < 85) {
            this.weatherState = Weather.OVERCAST;
        } else {
            this.weatherState = Weather.RAINY;
        }

        logWeatherChange("RANDOMIZED: Weather state code " + this.weatherState);
        repaint();
    }

    public void changeWeather(int newWeather) throws InvalidWeatherException {
        Weather.validate(newWeather);
        
        this.weatherState = newWeather;
        logWeatherChange("MANUAL CHANGE: Weather set to " + newWeather);
        repaint();
    }

    public void changeTime(int newTime) throws InvalidWeatherException {
        TimeOfDay.validate(newTime);
        
        this.timeState = newTime;
        repaint();
    }

    private void logWeatherChange(String logEntry) {
        weatherLog.add(logEntry);
    }

    public List<String> getWeatherLog() {
        return new ArrayList<>(weatherLog);
    }

    // Environmental API (Queried by Plants)

    public double getGrowthMultiplier() throws InvalidWeatherException {
        return TimeOfDay.growthFactor(this.timeState) * Weather.growthFactor(this.weatherState);
    }

    public boolean isRaining() {
        return this.weatherState == Weather.RAINY;
    }

    public boolean isShaded() {
        return this.weatherState == Weather.OVERCAST || this.timeState == TimeOfDay.NIGHT;
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

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        Color skyColor = getSkyColor();
        g2d.setColor(skyColor);
        g2d.fillRect(0, 0, width, height);

        drawCelestialBody(g2d, width);

        if (weatherState != Weather.SUNNY) {
            drawClouds(g2d);
        }
        if (weatherState == Weather.RAINY) {
            drawRain(g2d, width, height);
        }
    }

    private Color getSkyColor() {
        Color baseColor = switch (timeState) {
            case TimeOfDay.DAWN -> new Color(228, 151, 89);
            case TimeOfDay.DAY -> new Color(135, 206, 235);
            case TimeOfDay.DUSK -> new Color(38, 83, 141);
            case TimeOfDay.NIGHT -> new Color(20, 25, 50);
            default -> new Color(135, 206, 235);
        };

        if (weatherState == Weather.OVERCAST || weatherState == Weather.RAINY) {
            return darkenColor(baseColor, 0.45f);
        } else if (weatherState == Weather.CLOUDY) {
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

        if (timeState == TimeOfDay.DAY || timeState == TimeOfDay.DAWN) {
            g2d.setColor(new Color(255, 220, 50));
            g2d.fillOval(x, y, size, size);
        } else {
            g2d.setColor(new Color(235, 235, 210));
            g2d.fillOval(x, y, size - 5, size - 5);
        }
    }

    private void drawClouds(Graphics2D g2d) {
        g2d.setColor(weatherState == Weather.RAINY ? new Color(90, 95, 110) : new Color(245, 245, 245, 210));

        g2d.fillOval(60, 30, 90, 40);
        g2d.fillOval(100, 15, 80, 45);
        g2d.fillOval(290, 35, 110, 45);
        g2d.fillOval(340, 20, 90, 50);
        g2d.fillOval(560, 25, 100, 40);
    }

    private void drawRain(Graphics2D g2d, int width, int height) {
        g2d.setColor(new Color(160, 200, 255, 180));
        for (int x = 15; x < width; x += 28) {
            int startY = random.nextInt(height / 2);
            g2d.drawLine(x, startY, x - 7, startY + 20);
        }
    }
}