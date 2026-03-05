package exception.room;

import exception.service.ServiceException;

public class RoomNotFoundException extends ServiceException {
    public RoomNotFoundException(String message) {
        super(message);
    }
}
