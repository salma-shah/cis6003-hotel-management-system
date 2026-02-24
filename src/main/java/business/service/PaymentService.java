package business.service;

import java.sql.SQLException;

public interface PaymentService {
    boolean processPayment(int billId, double amount, String paymentMethod) throws SQLException;
}

