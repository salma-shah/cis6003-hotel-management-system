package dto;

public class ReservationAggregateDTO implements SuperDTO {
    private final ReservationDTO reservationDTO;
    private final BillDTO billDTO;
    private final PaymentDTO paymentDTO;
    private final GuestDTO guestDTO;
    private final String roomTypeName;
    private final int roomNum;

    public ReservationAggregateDTO(ReservationDTO reservationDTO, BillDTO billDTO, PaymentDTO paymentDTO, GuestDTO guestDTO, String roomTypeName
    , int roomNum) {
        this.reservationDTO = reservationDTO;
        this.billDTO = billDTO;
        this.guestDTO = guestDTO;
        this.paymentDTO = paymentDTO;
        this.roomTypeName = roomTypeName;
        this.roomNum = roomNum;
    }

    public ReservationDTO getReservationDTO() {
        return reservationDTO;
    }

    public BillDTO getBillDTO() {
        return billDTO;
    }

    public GuestDTO getGuestDTO() {
        return guestDTO;
    }

    public PaymentDTO getPaymentDTO() {
        return paymentDTO;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }
    public int getRoomNum() {
        return roomNum;
    }
}
