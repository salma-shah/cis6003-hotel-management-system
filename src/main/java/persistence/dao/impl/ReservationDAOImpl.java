package persistence.dao.impl;

import db.DBConnection;
import entity.Reservation;
import persistence.dao.ReservationDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationDAOImpl implements ReservationDAO {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(ReservationDAOImpl.class.getName());

    @Override
    public boolean add(Reservation entity) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
         return QueryHelper.execute(conn, "INSERT INTO reservation (reservation_number, guest_id, room_id, num_adults, num_children, total_cost, date_of_res, checkin_date, checkout_date,status) " +
                 "VALUES (?,?,?,?,?,?,?,?,?,?)",
                 entity.getReservationNumber(), entity.getGuestId(), entity.getRoomId(),
                 entity.getNumOfAdults(), entity.getNumOfChildren(), entity.getTotalCost(),
                 entity.getDateOfReservation(), entity.getCheckInDate(),entity.getCheckOutDate(), entity.getStatus());
        }
        catch(SQLException ex)
        {
            LOG.log(Level.INFO, "Something went wrong making the reservation in the DAO layer");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean update(Reservation entity) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        return false;
    }

    @Override
    public Reservation searchById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Reservation> getAll(Map<String, String> searchParams) throws SQLException {
        return List.of();
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey) throws SQLException {
        return false;
    }
}
