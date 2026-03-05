package exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException() { super();}
    public DataAccessException(String message) {
        super(message);
    }  // throws msg
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);  // throws msg and cause
    }
    public DataAccessException(Throwable cause) {
        super(cause);  // throws cause
    }
}
