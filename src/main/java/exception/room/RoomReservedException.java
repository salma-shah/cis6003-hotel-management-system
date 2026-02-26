package exception.room;

import exception.service.ServiceException;

public class RoomReservedException extends ServiceException {
    public RoomReservedException(String message) {
        super(message);
    }
}
