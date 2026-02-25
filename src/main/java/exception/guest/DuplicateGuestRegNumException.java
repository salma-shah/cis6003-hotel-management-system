package exception.guest;

public class DuplicateGuestRegNumException extends RuntimeException {
    public DuplicateGuestRegNumException(String message) {
        super(message);
    }
}
