package mapper;

import dto.ReservationDTO;
import entity.Reservation;

public class ReservationMapper {
    public static Reservation toReservation(ReservationDTO reservationDTO) {
        if  (reservationDTO == null) {
            return null;
        }

        return new Reservation(reservationDTO.getId(), reservationDTO.getGuestId(), reservationDTO.getRoomId(), reservationDTO.getReservationNumber(),
                reservationDTO.getTotalCost(), reservationDTO.getDateOfReservation(), reservationDTO.getCheckInDate(), reservationDTO.getCheckOutDate(), reservationDTO.getNumOfAdults(), reservationDTO.getNumOfChildren(), reservationDTO.getStatus());
    }

    public static ReservationDTO toReservationDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        return new ReservationDTO(reservation.getId(), reservation.getGuestId(), reservation.getRoomId(), reservation.getReservationNumber(), reservation.getTotalCost(),
                reservation.getDateOfReservation(), reservation.getCheckInDate(), reservation.getCheckOutDate(), reservation.getNumOfAdults(), reservation.getNumOfChildren(), reservation.getStatus());
    }
}
