package dto;

import java.util.List;

public class GuestHistoryDTO implements SuperDTO {
    private final int guestId;
    private final List<ReservationHistoryDTO> reservations;

    public GuestHistoryDTO(int guestId, List<ReservationHistoryDTO> reservations) {
        this.guestId = guestId;
        this.reservations = reservations;
    }
    public int getGuestId() {
        return guestId;
    }
    public List<ReservationHistoryDTO> getReservations() {
        return reservations;
    }

}
