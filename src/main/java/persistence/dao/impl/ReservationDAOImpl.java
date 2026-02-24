package persistence.dao.impl;

import constant.PaymentStatus;
import constant.ReservationStatus;
import db.DBConnection;
import dto.*;
import entity.Reservation;
import persistence.dao.ReservationDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            List<Reservation> reservations = new ArrayList<>();
            StringBuilder query = new StringBuilder("SELECT * FROM reservation WHERE 1=1");

            // these are the search queries
            List<Object> params = new ArrayList<>();
            if (searchParams != null && !searchParams.isEmpty()) {
                for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    // appending the search values to the sql
                    if ("reservation_number".equals(key)) {
                        query.append(" AND ").append(key).append(" LIKE ? ");
                        params.add("%" + value + "%");
                    }
                    if ("status".equals(key)) {
                        query.append(" AND status = ?");
                        params.add(value);
                    }
//                    if (searchParams.containsKey("bill_status")) {
//                        query.append(" AND b.bill_status = ?");
//                        params.add(value);
//                    }

                    // adding filter by checkin/checkout dates
                    if (searchParams.containsKey("checkin_date") && searchParams.containsKey("checkout_date")) {
                        query.append(" AND checkin_date < ? AND checkout_date > ? ");
                        params.add(searchParams.get("checkout_date"));
                        params.add(searchParams.get("checkin_date"));
                    }
                }
            }
            // now executing the query and mapping
                ResultSet resultSet = QueryHelper.execute(conn, query.toString(), params.toArray());
                while (resultSet.next()) {

                        Reservation reservation = mapResultSetToReservation(resultSet);
                        reservations.add(reservation);
                    }
                return reservations;
            }
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
    public Reservation findByReservationNumber(String resNum) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection()) {
            LOG.info("Running query with resNum: " + resNum);
            ResultSet resultSet = QueryHelper.execute(conn, "SELECT * FROM reservation WHERE reservation_number = ?", resNum);
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

    public Integer findReservationIdByReservationNumber(String resNum) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            ResultSet resultSet = QueryHelper.execute(conn, "SELECT id FROM reservation WHERE reservation_number = ?", resNum);
            if (!resultSet.next())
            {
                return null;
            }
            return resultSet.getInt("id");

        }
        catch(SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding the reservation by res number");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public ReservationAggregrateDTO findFullReservation(int id) throws SQLException {
        String sql = "SELECT r.id AS r_id, r.guest_id AS r_guest_id, r.room_id, r.reservation_number, r.checkin_date, r.checkout_date, r.total_cost," +
                "r.num_adults, r.num_children, r.status AS res_status, r.date_of_res, " +
                "b.id AS b_id, b.reservation_id, b.guest_id AS b_guest_id, b.stay_cost, b.total_amount, b.tax, b.discount, b.status AS bill_status, " +
                "p.payment_id, p.bill_id, p.payment_method, p.payment_date, p.amount " +
                "FROM reservation r " +
                " LEFT JOIN bill b ON r.id = b.reservation_id " +
                " LEFT JOIN payment p ON b.id = p.bill_id " +
                " WHERE r.id = ?";
        try(Connection conn = DBConnection.getInstance().getConnection()) {
            ResultSet resultSet = QueryHelper.execute(conn, sql, id);
            if (!resultSet.next()) {
                return null;
            }

            LocalDateTime dateTime = LocalDateTime.now();
            ReservationDTO reservationDTO = new ReservationDTO(
                    resultSet.getInt("r_id"), resultSet.getInt("r_guest_id"), resultSet.getInt("room_id"),
                    resultSet.getString("reservation_number"), resultSet.getDouble("total_cost"), dateTime,
                    resultSet.getDate("checkin_date").toLocalDate(),
                    resultSet.getDate("checkout_date").toLocalDate(),
                    resultSet.getInt("num_adults"),
                    resultSet.getInt("num_children"),
                    ReservationStatus.valueOf(resultSet.getString("res_status")));

            BillDTO billDTO = null;
            if (resultSet.getObject("b_id") != null) {
                billDTO = new BillDTO(resultSet.getInt("b_id"), resultSet.getInt("reservation_id"),
                        resultSet.getInt("b_guest_id"),
                        resultSet.getDouble("stay_cost"), resultSet.getDouble("tax"),
                        resultSet.getDouble("discount"),
                        resultSet.getDouble("total_amount"),
                        PaymentStatus.valueOf(resultSet.getString("bill_status")));
            }

            PaymentDTO paymentDTO = null;
            if (resultSet.getObject("p.payment_id") != null) {
                paymentDTO =new PaymentDTO(
                        resultSet.getInt("payment_id"), resultSet.getInt("bill_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("payment_date").toLocalDateTime(),
                        resultSet.getString("payment_method"));
            }
            return new ReservationAggregrateDTO(reservationDTO, billDTO, paymentDTO);
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
