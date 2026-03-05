package exception;

import exception.db.DataAccessException;

import java.sql.SQLIntegrityConstraintViolationException;

public class ConstraintViolationException extends DataAccessException {
    public ConstraintViolationException(String message, SQLIntegrityConstraintViolationException ex) {
        super(message);
    }
}
