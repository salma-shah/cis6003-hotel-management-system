package exception.room;

public class AmenitiesNotFoundException extends RuntimeException {
    public AmenitiesNotFoundException(String message) {
        super(message);
    }
}
