package dto;

import constant.PaymentStatus;

import java.util.List;

public class ReservationHistoryDTO {
    private final ReservationDTO reservations;
    private final double totalAmount;
    private final PaymentStatus paymentStatus;
    private final String roomTypeName;
    public ReservationHistoryDTO(ReservationDTO reservations, double totalAmount, PaymentStatus paymentStatus, String roomTypeName) {
        this.reservations = reservations;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.roomTypeName = roomTypeName;
    }
    public ReservationDTO getReservations() {
        return reservations;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    public String getRoomTypeName() {
        return roomTypeName;
    }
}
