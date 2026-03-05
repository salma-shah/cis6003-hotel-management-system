package exception.user;

import exception.service.ServiceException;

public class DuplicateUsernameException extends ServiceException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
