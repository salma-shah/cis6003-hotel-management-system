package persistence.dao.impl;

import constant.PaymentStatus;
import constant.ReservationStatus;
import db.DBConnection;
import dto.*;
import entity.Reservation;
import exception.ConstraintViolationException;
import exception.db.DataAccessException;
import exception.DuplicateEntityException;
import exception.EntityNotFoundException;
import persistence.dao.ReservationDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.*;
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
    public boolean add(Reservation entity)  {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeUpdate(conn, "INSERT INTO reservation (reservation_number, guest_id, room_id, num_adults, num_children, total_cost, date_of_res, checkin_date, checkout_date,status) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?)",
                    entity.getReservationNumber(), entity.getGuestId(), entity.getRoomId(),
                    entity.getNumOfAdults(), entity.getNumOfChildren(), entity.getTotalCost(),
                    entity.getDateOfReservation(), entity.getCheckInDate(), entity.getCheckOutDate(), entity.getStatus().name());
        } catch (SQLException ex) {
            throw new DataAccessException("Error making the reservation", ex);
        }
    }

    @Override
    public boolean update(Reservation entity) {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeUpdate(conn, "UPDATE reservation SET checkout_date, total_cost WHERE reservation_id = ?", entity.getId());
        } catch (SQLException ex) {
            throw new DataAccessException("Error updating the reservation", ex);
        }
    }

    @Override
    public boolean delete(int id)  {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeUpdate(conn, "DELETE FROM reservation WHERE id = ?", id);
        }
        catch (SQLException ex) {
            throw new DataAccessException("Error deleting the reservation", ex);
        }
    }

    @Override
    public Reservation searchById(int id)  {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
           return QueryHelper.executeQuery(conn, "SELECT * FROM reservation WHERE id = ?",
                   rs -> {
               if (rs.next()) {
                   return mapResultSetToReservation(rs);
               } throw new EntityNotFoundException("Reservation with id: " + id + " not found");
                   },
                   id);
        }
        catch (SQLException ex) {
            throw new EntityNotFoundException(" Reservation not found: " + id, ex);
        }
    }

    @Override
    public List<Reservation> getAll(Map<String, String> searchParams) {

        List<Reservation> reservations = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM reservation WHERE 1=1");

        // these are the search queries
        List<Object> params = new ArrayList<>();
        if (searchParams != null && !searchParams.isEmpty()) {
            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                // appending the search values to the SQL
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

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            // now executing the query and mapping
            return QueryHelper.executeQuery(conn, query.toString(),
                    rs -> {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                } return reservations;
                    },
                    params.toArray());
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error getting all reservations", ex);
        }

    }


    @Override
    public boolean existsByPrimaryKey(int primaryKey)  {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeQuery(conn,
                    "SELECT 1 FROM reservation WHERE id=?",
                    rs -> rs.next(), primaryKey);
        } catch (SQLException ex) {
            throw new EntityNotFoundException("Error finding the reservation with PK: " + primaryKey, ex);
        }
    }

    @Override
    public Reservation findByReservationNumber(String resNum)  {
        try(Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeQuery(conn, "SELECT * FROM reservation WHERE reservation_number = ?",
                    rs ->
                    {
                        if (rs.next()) {
                            return mapResultSetToReservation(rs);
                        } throw new EntityNotFoundException("Reservation with resNum: " + resNum + " not found");
                    },
                    resNum);
        }
        catch (SQLException ex) {
            throw new EntityNotFoundException("Error finding the reservation with res num" + resNum, ex);
        }
    }

    @Override
    public boolean validateReservationNumber(String resNum)  {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeQuery(conn, "SELECT 1 FROM reservation WHERE reservation_number = ?",
                    rs -> {
                if (rs.next()) {
                   return rs.next();
                } return false;
                    },
                    resNum);
        }
        catch (SQLException ex) {
            throw new DuplicateEntityException("Reservation number: " + resNum + " already exists", ex);
        }
    }

    public Integer findReservationIdByReservationNumber(String resNum)  {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeQuery(conn, "SELECT id FROM reservation WHERE reservation_number = ?",
                    rs -> {
                if  (rs.next()) {
                    return rs.getInt("id");
                }throw new EntityNotFoundException("Reservation not found:" + resNum);
                    },
                    resNum);

        }
        catch (SQLException ex) {
            throw new EntityNotFoundException("Error finding the reservation with reservation number: " + resNum, ex);
        }
    }

    @Override
    public ReservationAggregateDTO findFullReservation(int id)  {
        String sql = "SELECT r.id AS r_id, r.guest_id AS r_guest_id, r.room_id, r.reservation_number, r.checkin_date, r.checkout_date, r.total_cost," +
                "r.num_adults, r.num_children, r.status AS res_status, r.date_of_res, " +
                "b.id AS b_id, b.reservation_id, b.guest_id AS b_guest_id, b.stay_cost, b.total_amount, b.tax, b.discount, b.status AS bill_status, " +
                "p.payment_id, p.bill_id, p.payment_method, p.payment_date, p.amount, " +
                "g.registration_number, g.id AS g_guest_id, g.first_name, g.last_name, g.address, g.email, g.nic, g.passport_number, g.contact_number, " +
                "rt.name AS rt_name, rm.room_number " +
                "FROM reservation r " +
                " LEFT JOIN bill b ON r.id = b.reservation_id " +
                " LEFT JOIN payment p ON b.id = p.bill_id " +
                " LEFT JOIN guest g ON r.guest_id = g.id " +
                " LEFT JOIN room rm ON r.room_id = rm.room_id " +
                " LEFT JOIN room_type rt ON rm.room_type_id = rt.room_type_id " +
                " WHERE r.id = ?";

        try(Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeQuery(conn, sql,
                    resultSet ->
                    {
                        if (!resultSet.next()) {
                            throw new EntityNotFoundException("Reservation not found for ID: " + id);
                        }

                        LocalDateTime dateTime = resultSet.getTimestamp("date_of_res").toLocalDateTime();
                        ReservationDTO reservationDTO = new ReservationDTO(
                                resultSet.getInt("r_id"), resultSet.getInt("r_guest_id"), resultSet.getInt("room_id"),
                                resultSet.getString("reservation_number"), resultSet.getDouble("total_cost"),
                                dateTime,
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
                        if (resultSet.getObject("payment_id") != null) {
                            paymentDTO = new PaymentDTO(
                                    resultSet.getInt("payment_id"), resultSet.getInt("bill_id"),
                                    resultSet.getDouble("amount"),
                                    resultSet.getTimestamp("payment_date").toLocalDateTime(),
                                    resultSet.getString("payment_method"));
                        }

                        String roomTypeName = resultSet.getString("rt_name");
                        int roomNum = resultSet.getInt("room_number");
                        // guest dto building
                        GuestDTO guestDTO = null;
                        if (resultSet.getObject("g_guest_id") != null) {
                            guestDTO = new GuestDTO.GuestDTOBuilder()
                                    .id(resultSet.getInt("g_guest_id"))
                                    .firstName(resultSet.getString("first_name"))
                                    .lastName(resultSet.getString("last_name"))
                                    .address(resultSet.getString("address"))
                                    .contactNumber(resultSet.getString("contact_number"))
                                    .nic(resultSet.getString("nic"))
                                    .passportNumber(resultSet.getString("passport_number"))
                                    .registrationNumber(resultSet.getString("registration_number"))
                                    .email(resultSet.getString("email")).build();
                        }
                        return new ReservationAggregateDTO(reservationDTO, billDTO, paymentDTO, guestDTO, roomTypeName, roomNum);
                    }, id);
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException("Error retrieving reservation's data from database", ex);
        }
    }

    @Override
    public boolean updateReservationStatus(int id, ReservationStatus status)  {
        try(Connection conn = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeUpdate(conn, "UPDATE reservation SET status = ? WHERE id = ?", status.toString(), id);
        }
        catch (SQLIntegrityConstraintViolationException ex) {
            throw new ConstraintViolationException("Invalid reservation status", ex);
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Failed to update reservation status", e);
        }
    }

    private Reservation mapResultSetToReservation(ResultSet resultSet)  {

        try {
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
        catch (SQLException ex) {
            throw new EntityNotFoundException("Error mapping the reservation", ex);
        }
    }

}
