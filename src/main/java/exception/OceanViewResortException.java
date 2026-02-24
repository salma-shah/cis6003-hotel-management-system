package exception;

public class OceanViewResortException extends RuntimeException {
    private final OceanViewResortExceptionTypes exceptionType;

    public OceanViewResortException(OceanViewResortExceptionTypes exceptionType) {
        super(exceptionType.name());
        this.exceptionType = exceptionType;
    }

    public String getExceptionType() {
        return String.valueOf(exceptionType);
    }
}
