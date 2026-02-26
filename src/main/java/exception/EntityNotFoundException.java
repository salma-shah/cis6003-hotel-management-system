package exception;

import exception.db.DataAccessException;

import java.sql.SQLException;

public class EntityNotFoundException extends DataAccessException {
    public EntityNotFoundException(String message, SQLException e) {
        super(message);
    }
    public EntityNotFoundException(String message) {}
}
