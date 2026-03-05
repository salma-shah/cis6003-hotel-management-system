package exception.bill;

import exception.service.ServiceException;

public class BillNotFoundException extends ServiceException {
    public BillNotFoundException(String message) {
        super(message);
    }
}
