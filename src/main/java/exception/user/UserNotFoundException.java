package exception.user;

import exception.service.ServiceException;

public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
