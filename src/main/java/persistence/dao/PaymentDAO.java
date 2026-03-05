package persistence.dao;

import entity.Payment;

public interface PaymentDAO {
    boolean insertPayment(Payment payment);
    Payment searchById(int id);
}
