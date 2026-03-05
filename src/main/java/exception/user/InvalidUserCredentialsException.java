package exception.user;

import exception.service.ServiceException;

public class InvalidUserCredentialsException extends ServiceException {
    public InvalidUserCredentialsException(String message) {
        super(message);
    }
}
