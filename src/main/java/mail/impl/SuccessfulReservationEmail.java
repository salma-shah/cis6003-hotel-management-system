package mail.impl;

import mail.EmailBase;

import java.time.LocalDate;

public class SuccessfulReservationEmail implements EmailBase {
    private final String name, email, reservationNum, roomTypeName;
    private final LocalDate checkInDate, checkOutDate;

    public SuccessfulReservationEmail(String name, String email, String reservationNum, String roomTypeName, LocalDate checkInDate, LocalDate checkOutDate) {
        this.name = name;
        this.email = email;
        this.reservationNum = reservationNum;
        this.roomTypeName = roomTypeName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String getReceiver() {
        return email;
    }

    @Override
    public String getSubject() {
        return "Ocean View Resort Reservation Confirmation ";
    }

    @Override
    public String getBody() {
        return "Dear " + name + ",\n\n" +

                "Thank you for choosing Ocean View Resort for your upcoming stay in Galle. " +
                "We are pleased to confirm your reservation and look forward to welcoming you.\n\n" +

                "Reservation Details:\n" +
                "----------------------------------------\n" +
                "Reservation Number: " + reservationNum + "\n" +
                "Check-in Date: " + checkInDate + "\n" +
                "Check-out Date: " + checkOutDate + "\n" +
                "Room Type: " + roomTypeName + "\n" +
                "----------------------------------------\n\n" +

                "Check-in time is from 2:00 PM onwards, and check-out is by 11:00 AM. " +
                "Please present a valid identification document upon arrival.\n\n" +

                "If you need to modify or cancel your reservation, please contact us in advance. " +
                "Our team is happy to assist you with any special requests.\n\n" +

                "We look forward to providing you with a comfortable and memorable stay.\n\n" +

                "Kind regards,\n" +
                "Ocean View Resort Team\n" +
                "Galle\n" +
                "Email: ocean.view.hotel.cis6003@gmail.com\n" +
                "Phone: +94 11 22 33 567";
    }
}
