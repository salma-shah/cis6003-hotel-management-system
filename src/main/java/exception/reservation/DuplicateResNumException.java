package exception.reservation;

public class DuplicateResNumException extends RuntimeException {
    public DuplicateResNumException(String message) {
        super(message);
    }
}
