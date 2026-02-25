package exception.reservation;

import exception.service.ServiceException;

public class ReservationNotFoundException extends ServiceException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
