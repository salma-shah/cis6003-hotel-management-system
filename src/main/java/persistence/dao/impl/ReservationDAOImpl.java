package persistence.dao.impl;

import constant.ReservationStatus;
import db.DBConnection;
import entity.Reservation;
import persistence.dao.ReservationDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationDAOImpl implements ReservationDAO {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(ReservationDAOImpl.class.getName());

    @Override
    public boolean add(Reservation entity) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.execute(conn, "INSERT INTO reservation (reservation_number, guest_id, room_id, num_adults, num_children, total_cost, date_of_res, checkin_date, checkout_date,status) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?)",
                    entity.getReservationNumber(), entity.getGuestId(), entity.getRoomId(),
                    entity.getNumOfAdults(), entity.getNumOfChildren(), entity.getTotalCost(),
                    entity.getDateOfReservation(), entity.getCheckInDate(), entity.getCheckOutDate(), entity.getStatus().name());
        } catch (SQLException ex) {
            LOG.log(Level.INFO, "Something went wrong making the reservation in the DAO layer");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean update(Reservation entity) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.execute(conn, "UPDATE reservation SET checkout_date, total_cost WHERE reservation_id = ?", entity.getId());
        } catch (SQLException ex) {
            LOG.log(Level.INFO, "Something went wrong updating the reservation in the DAO layer");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.execute(conn, "DELETE FROM reservation WHERE id = ?", id);
        }
    }

    @Override
    public Reservation searchById(int id) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            ResultSet resultSet = QueryHelper.execute(conn, "SELECT * FROM reservation WHERE id = ?", id);
            if (!resultSet.next()) {
                return null;
            }
            return mapResultSetToReservation(resultSet);
        }
    }

    @Override
    public List<Reservation> getAll(Map<String, String> searchParams) throws SQLException {
        return List.of();
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            ResultSet resultSet = QueryHelper.execute(conn,
                    "SELECT 1 FROM reservation WHERE id=?", primaryKey);
            return resultSet.next();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "There was an error finding an existing reservation by primary key.");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public Reservation findByReservationNumber() throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection()) {
            ResultSet resultSet = QueryHelper.execute(conn, "SELECT 1 FROM reservation WHERE reservation_number = ?", 1);
            if (!resultSet.next()) {
                return null;
            }
            return mapResultSetToReservation(resultSet);
        }
    }

    @Override
    public boolean validateReservationNumber(String resNum) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            ResultSet resultSet = QueryHelper.execute(conn, "SELECT 1 FROM reservation WHERE reservation_number = ?", resNum);
            if (!resultSet.next()) {
                return false;
            }
            return resultSet.next();
        }
    }

    private Reservation mapResultSetToReservation(ResultSet resultSet) throws SQLException {

        Timestamp timestamp = resultSet.getTimestamp("date_of_res");
        LocalDateTime dateOfRes = null;

        if (timestamp != null) {
            dateOfRes = timestamp.toLocalDateTime();
        }

        return new Reservation(
                resultSet.getInt("id"),
                resultSet.getInt("guest_id"),
                resultSet.getInt("room_id"),
                resultSet.getString("reservation_number"),
                resultSet.getDouble("total_cost"),
                dateOfRes,
                resultSet.getDate("checkin_date").toLocalDate(),
                resultSet.getDate("checkout_date").toLocalDate(),
                resultSet.getInt("num_adults"),
                resultSet.getInt("num_children"),
                ReservationStatus.valueOf(resultSet.getString("status"))
        );
    }

}
