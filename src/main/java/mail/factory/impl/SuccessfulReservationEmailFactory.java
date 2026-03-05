package mail.factory.impl;

import dto.GuestDTO;
import dto.ReservationDTO;
import dto.RoomTypeDTO;
import mail.EmailBase;
import mail.factory.EmailFactory;
import mail.impl.SuccessfulReservationEmail;

import java.time.LocalDate;

public class SuccessfulReservationEmailFactory extends EmailFactory {
    private final ReservationDTO reservationDTO;
    private final GuestDTO guestDTO;
    private final String roomTypeName;

    public SuccessfulReservationEmailFactory(ReservationDTO reservationDTO, GuestDTO guestDTO, String roomTypeName) {
        this.reservationDTO = reservationDTO;
        this.guestDTO = guestDTO;
        this.roomTypeName = roomTypeName;
    }

    @Override
    public EmailBase createEmail() {
        return new SuccessfulReservationEmail((guestDTO.getFirstName() + " " + guestDTO.getLastName()), guestDTO.getEmail(), reservationDTO.getReservationNumber(),
                roomTypeName, reservationDTO.getCheckInDate(), reservationDTO.getCheckOutDate());
    }
}
