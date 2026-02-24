package dto;

public class ReservationAggregrateDTO {
    private final ReservationDTO reservationDTO;
    private final BillDTO billDTO;
    private final PaymentDTO paymentDTO;
    private final GuestDTO guestDTO;

    public ReservationAggregrateDTO(ReservationDTO reservationDTO, BillDTO billDTO, PaymentDTO paymentDTO, GuestDTO guestDTO) {
        this.reservationDTO = reservationDTO;
        this.billDTO = billDTO;
        this.guestDTO = guestDTO;
        this.paymentDTO = paymentDTO;
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
}
