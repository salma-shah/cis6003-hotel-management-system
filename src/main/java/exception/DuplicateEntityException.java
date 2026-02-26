package exception;

import exception.db.DataAccessException;

import java.sql.SQLException;

public class DuplicateEntityException extends DataAccessException {
    public DuplicateEntityException(String message, SQLException ex) {
        super(message);
    }
}
