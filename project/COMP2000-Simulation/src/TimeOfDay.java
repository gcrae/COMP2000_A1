public final class TimeOfDay {
    
    // Prevent instantiation
    private TimeOfDay() {}
    
    public static final int DAWN = 0;
    public static final int DAY = 1;
    public static final int DUSK = 2;
    public static final int NIGHT = 3;

    private static final double[] GROWTH_FACTOR = {0.8, 1.5, 0.8, 0.2};

    public static void validate(int code) throws InvalidWeatherException {
        if (code < DAWN || code > NIGHT) {
            throw new InvalidWeatherException("Time of day code " + code + " is invalid. Must be between 0 and 3.");
        }
    }

    public static double growthFactor(int code) throws InvalidWeatherException {
        validate(code);
        return GROWTH_FACTOR[code];
    }
}