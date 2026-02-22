package persistence.dao.impl;

import constant.PaymentStatus;
import db.DBConnection;

import entity.Bill;
import persistence.dao.BillDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillDAOImpl implements BillDAO {
    private static final Logger LOG = Logger.getLogger(BillDAOImpl.class.getName());

    public int generateBill(Bill entity) throws SQLException {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    String.valueOf("INSERT INTO bill (reservation_id, guest_id, stay_cost,tax, discount, total_amount, created_at) VALUES ( ?, ?,?, ?, ?, ?,?)"),
                    Statement.RETURN_GENERATED_KEYS);
            {
                ps.setInt(1, entity.getResId());
                ps.setInt(2, entity.getGuestId());
                ps.setDouble(3, entity.getStayCost());
                ps.setDouble(5, entity.getTax());
                ps.setDouble(6, entity.getDiscount());
                ps.setDouble(4, entity.getTotalAmount());
                ps.setObject(7, LocalDateTime.now());
                int rows = ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                else
                {
                    throw new SQLException("No generated key generated");
                }
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "There was an error generating the bill");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public Bill searchById(int id) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            ResultSet resultSet = QueryHelper.execute(conn, "SELECT * FROM bill WHERE id = ?", id);
            if (!resultSet.next()) {
                return null;
            }
            return mapResultSetToBill(resultSet);
        }

    }

    @Override
    public int getLastInsertedId() throws SQLException {
        String sql = "SELECT LAST_INSERT_ID()"; // mysql syntax
        try (Connection connection = DBConnection.getInstance().getConnection())
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Unable to retrieve last inserted ID");
            }
        }
    }

    private Bill mapResultSetToBill(ResultSet resultSet) throws SQLException {
        return new Bill(resultSet.getInt("id"), resultSet.getInt("reservation_id"), resultSet.getInt("guest_id"),
                 resultSet.getDouble("tax"), resultSet.getDouble("discount"), resultSet.getDouble("total_amount"),
                resultSet.getDouble("stay_cost")
        );
    }
}
