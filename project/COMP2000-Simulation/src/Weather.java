public final class Weather {
    
    private Weather() {}
    
    public static final int SUNNY = 0;
    public static final int CLOUDY = 1;
    public static final int OVERCAST = 2;
    public static final int RAINY = 3;

    private static final double[] GROWTH_FACTOR = {1.0, 0.9, 0.7, 1.4};

    public static void validate(int code) throws InvalidWeatherException {
        if (code < SUNNY || code > RAINY) {
            throw new InvalidWeatherException("Weather code " + code + " is invalid. Must be between 0 and 3.");
        }
    }

    public static double growthFactor(int code) throws InvalidWeatherException {
        validate(code);
        return GROWTH_FACTOR[code];
    }
}