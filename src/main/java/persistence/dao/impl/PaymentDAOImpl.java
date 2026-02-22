package persistence.dao.impl;

import db.DBConnection;
import entity.Payment;
import persistence.dao.PaymentDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean insertPayment(Payment payment) throws SQLException {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.execute(connection, "INSERT INTO payment (bill_id, amount, payment_method, payment_date) " +
                    "VALUES (?,?,?,?)", payment.getBillId(), payment.getAmount(), payment.getPaymentMethod(), payment.getPaymentDate());
        }
    }

    @Override
    public Payment searchById(int id) throws SQLException {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            ResultSet resultSet = QueryHelper.execute(connection, "SELECT * FROM payment WHERE payment_id = ?", id);
            if (!resultSet.next()) {
                return null;
            }
            return mapResultSetToPayment(resultSet);
        }
    }

    private Payment mapResultSetToPayment(ResultSet resultSet) throws SQLException {
        LocalDateTime paymentDate = resultSet.getTimestamp("payment_date").toLocalDateTime();
        return new Payment(
                resultSet.getInt("payment_id"), resultSet.getInt("bill_id"),
                resultSet.getDouble("amount"),
                paymentDate,
                resultSet.getString("payment_method"));
    }
}
