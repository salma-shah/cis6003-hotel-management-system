package persistence.dao.impl;

import db.DBConnection;
import entity.Payment;
import exception.db.DataAccessException;
import exception.EntityNotFoundException;
import persistence.dao.PaymentDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean insertPayment(Payment payment) {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(connection, "INSERT INTO payment (bill_id, amount, payment_method, payment_date) " +
                    "VALUES (?,?,?,?)", payment.getBillId(), payment.getAmount(), payment.getPaymentMethod(), payment.getPaymentDate());
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public Payment searchById(int id) {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeQuery(connection, "SELECT * FROM payment WHERE payment_id = ?",
                    rs ->

                    {
                        if (rs.next()) {
                            return mapResultSetToPayment(rs);
                        }
                        return null;
                    }, id);
        }
        catch (SQLException e)
        {
            throw new EntityNotFoundException("Payment for" + id + " was not found", e);
        }
    }

    private Payment mapResultSetToPayment(ResultSet resultSet){
        try {
            LocalDateTime paymentDate = resultSet.getTimestamp("payment_date").toLocalDateTime();
            return new Payment(
                    resultSet.getInt("payment_id"), resultSet.getInt("bill_id"),
                    resultSet.getDouble("amount"),
                    paymentDate,
                    resultSet.getString("payment_method"));
        }
        catch (SQLException e){
            throw new EntityNotFoundException("Error finding payment object", e);
        }
    }
}
