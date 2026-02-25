package exception.service;

public class DuplicateEmailException extends ServiceException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
