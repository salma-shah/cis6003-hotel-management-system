package business.service;

public interface PaymentService {
    boolean processPayment(int billId, double amount, String paymentMethod);
}

