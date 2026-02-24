package exception;

public enum OceanViewResortExceptionTypes {
    // user related exceptions
    USER_NOT_FOUND("User was not found"),
    INVALID_USER_INPUTS("User input fields are not valid"),
    INVALID_USER_CREDENTIALS("User credentials are invalid"),
    USER_ALREADY_EXISTS("User already exists"),
    INVALID_USER_ROLE("User role is invalid"),
    CANNOT_CHANGE_USERNAME("Username cannot be changed"),
    CANNOT_CHANGE_EMAIL("Email cannot be changed"),
    INVALID_PASSWORD("Password could not be changes"),

    // guest related exceptions
    GUEST_NOT_FOUND("Guest was not found"),
    INVALID_GUEST_INPUTS("Guest input fields are not valid"),
    GUEST_REGNUM_ALREADY_EXISTS("The guest registration number already exists"),
    EMPTY_NIC_AND_PASSPORT("NIC or Passport number is required"),
    GUEST_EMAIL_EXISTS("The guest email already exists"),
    GUEST_NIC_EXISTS("The guest NIC already exists"),

    // room
    ROOM_NOT_FOUND("Room was not found"),
    INVALID_ROOM_INPUTS("Room input fields are not valid"),
    ROOM_UNAVAILABLE("Room is not available"),
    ROOM_ALREADY_EXISTS("Room already exists"),

    // reservations
    RESERVATION_NOT_FOUND("Reservation was not found"),
    INVALID_RESERVATION_INPUTS("Reservation input fields are not valid"),
    RESERVATION_NUM_ALREADY_EXISTS("The reservation number already exists"),
    EMPTY_FIELDS("All fields are required to make a reservation"),
    RESERVATION_DATE_CONFLICTS("There is already a reservation for these dates"),
    ERROR_RESERVATION_UPDATE("There was an error updating the reservation"),
    ERROR_RESERVATION_ADD("There was an error adding the reservation"),
    RESERVATION_ALREADY_CANCELLED("The reservation has already been cancelled"),

    // bill
    BILL_NOT_FOUND("Bill was not found"),
    BILL_ALREADY_EXISTS("Bill already exists"),
    BILL_PDF_FAILED_TO_GENERATE("The bill PDF could not be generated"),
    INVALID_BILL_INPUT("Bill input fields are not valid"),
    BILL_FAILED_CALCULATE("The bill could not be calculated"),

    // payment
    PAYMENT_FAILED("Payment failed"),

    // general
    DATABASE_ERROR("Database error"),
    SYSTEM_ERROR("System error");

    private final String message;
    OceanViewResortExceptionTypes(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
