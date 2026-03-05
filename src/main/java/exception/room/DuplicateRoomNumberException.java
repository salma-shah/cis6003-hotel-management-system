package exception.room;

import exception.service.ServiceException;

public class DuplicateRoomNumberException extends ServiceException {
    public DuplicateRoomNumberException(String message) {
        super(message);
    }
}
