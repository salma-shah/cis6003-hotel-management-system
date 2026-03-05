package exception.service;

public class DateInPastException extends ServiceException {
    public DateInPastException(String message) {
        super(message);
    }
}
