package exception.service;

public class CheckOutDateBeforeCheckInException extends ServiceException {
    public CheckOutDateBeforeCheckInException(String message) {
        super(message);
    }
}
