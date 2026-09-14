//Custom checked exception thrown when an invalid weather state code is provided.
public class InvalidWeatherException extends Exception {
    public InvalidWeatherException(String message) {
        super(message);
    }
}