package exception.service;

public abstract class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
