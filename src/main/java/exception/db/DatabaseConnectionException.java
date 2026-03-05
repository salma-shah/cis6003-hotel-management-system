package exception.db;

public class DatabaseConnectionException extends DataAccessException {
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
