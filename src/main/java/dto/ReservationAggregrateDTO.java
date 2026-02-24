package dto;

public class ReservationAggregrateDTO {
    private final ReservationDTO reservationDTO;
    private final BillDTO billDTO;
    private final PaymentDTO paymentDTO;

    public ReservationAggregrateDTO(ReservationDTO reservationDTO, BillDTO billDTO, PaymentDTO paymentDTO) {
        this.reservationDTO = reservationDTO;
        this.billDTO = billDTO;
        this.paymentDTO = paymentDTO;
    }

    public ReservationDTO getReservationDTO() {
        return reservationDTO;
    }

    public BillDTO getBillDTO() {
        return billDTO;
    }

    public PaymentDTO getPaymentDTO() {
        return paymentDTO;
    }
}
