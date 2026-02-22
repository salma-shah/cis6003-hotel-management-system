package persistence.dao;

import entity.Payment;

import java.sql.SQLException;

public interface PaymentDAO {
    boolean insertPayment(Payment payment) throws SQLException;
    Payment searchById(int id) throws SQLException;
}
