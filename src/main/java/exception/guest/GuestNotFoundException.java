package exception.guest;

import exception.service.ServiceException;

public class GuestNotFoundException extends ServiceException {
    public GuestNotFoundException(String message) {
        super(message);
    }
}
