package mapper;

import dto.PaymentDTO;
import entity.Payment;
import entity.Reservation;

public class PaymentMapper {
    public static Payment toPayment(PaymentDTO paymentDTO) {
        if (paymentDTO == null) {
        return  null;
        }

        return new Payment(paymentDTO.getId(), paymentDTO.getBillId(), paymentDTO.getAmount(), paymentDTO.getPaymentDate(), paymentDTO.getPaymentMethod());

    }

    public static PaymentDTO toPaymentDTO(Payment payment) {
        if (payment == null) {
            return  null;
        }

        return new PaymentDTO(payment.getId(), payment.getBillId(), payment.getAmount(), payment.getPaymentDate(), payment.getPaymentMethod());
    }
}
