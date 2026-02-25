package persistence.dao.impl;

import db.DBConnection;

import entity.Bill;
import exception.db.DataAccessException;
import exception.EntityNotFoundException;
import persistence.dao.BillDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class BillDAOImpl implements BillDAO {
    private static final Logger LOG = Logger.getLogger(BillDAOImpl.class.getName());

    public int generateBill(Bill entity) {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    String.valueOf("INSERT INTO bill (reservation_id, guest_id, stay_cost,tax, discount, total_amount, created_at) VALUES ( ?, ?,?, ?, ?, ?,?)"),
                    Statement.RETURN_GENERATED_KEYS);
            {
                ps.setInt(1, entity.getResId());
                ps.setInt(2, entity.getGuestId());
                ps.setDouble(3, entity.getStayCost());
                ps.setDouble(4, entity.getTax());
                ps.setDouble(5, entity.getDiscount());
                ps.setDouble(6, entity.getTotalAmount());
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
            throw new DataAccessException("There was an error inserting the bill", ex);
        }
    }

    @Override
    public Bill searchById(int id) {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeQuery(conn, "SELECT * FROM bill WHERE id = ?",
                    rs -> {
                if (rs.next()) {
                    return mapResultSetToBill(rs);
                }
                    return null;
                },
                    id);
        } catch (SQLException e) {
            throw new EntityNotFoundException("Bill for id could not be found", e);
        }

    }

    private Bill mapResultSetToBill(ResultSet resultSet) {

        try {
            return new Bill(resultSet.getInt("id"), resultSet.getInt("reservation_id"), resultSet.getInt("guest_id"),
                    resultSet.getDouble("stay_cost"),
                    resultSet.getDouble("tax"), resultSet.getDouble("discount"),
                    resultSet.getDouble("total_amount")
            );
        }
        catch (SQLException ex) {
            throw new EntityNotFoundException(ex.getMessage());
        }
    }
}
