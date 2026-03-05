package exception.guest;

import exception.service.ServiceException;

public class DuplicateNicException extends ServiceException {
    public DuplicateNicException(String message) {
        super(message);
    }
}
