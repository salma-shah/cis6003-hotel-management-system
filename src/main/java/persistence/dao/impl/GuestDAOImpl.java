package persistence.dao.impl;

import constant.PaymentStatus;
import constant.ReservationStatus;
import db.DBConnection;
import dto.GuestHistoryDTO;
import dto.ReservationDTO;
import dto.ReservationHistoryDTO;
import entity.Guest;
import exception.db.DataAccessException;
import exception.EntityNotFoundException;
import persistence.dao.GuestDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuestDAOImpl implements GuestDAO {

    Logger LOG =  Logger.getLogger(GuestDAOImpl.class.getName());
    @Override
    public boolean existsByRegistrationNumber(String registrationNumber)  {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT 1 FROM guest WHERE registration_number = ?",
                    rs -> rs.next(), registrationNumber);
        }
        catch(SQLException ex)
        {
            throw new DataAccessException("There was an error finding the guest by reg number", ex);
        }
    }

    @Override
    public Integer findGuestIdByRegistrationNumber(String registrationNumber)  {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT id FROM guest WHERE registration_number = ?", rs -> {
                        if (rs.next()) {
                            return rs.getInt("id");
                        }
                        return null;
                    },
                    registrationNumber);
        }
        catch(SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding the guest by reg number", ex);
            throw new DataAccessException("There was an error finding the guest by reg number", ex);
        }
    }

    public String findGuestRegNumById(int id)  {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT registration_number FROM guest WHERE id = ?", rs ->
            {
                if (rs.next()) {
                    return rs.getString("registration_number");
                }
                return null;
            }, id);

        }
        catch(SQLException ex)
        {
            throw new DataAccessException("There was an error finding the guest by ID", ex);
        }
    }

    @Override
    public boolean findByEmail(String email)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(conn,
                    "SELECT 1 FROM guest WHERE email=?", rs -> rs.next(), email);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("There was an error finding the guest by email", ex);
        }
    }

    @Override
    public boolean add(Guest entity)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(conn, "INSERT INTO guest (registration_number, first_name, last_name, contact_number, " +
                    "address, email, nic, passport_number, dob, nationality) VALUES (?, ?,?, ?, ?, ?, ?, ?, ?, ?)", entity.getRegistrationNumber(),entity.getFirstName(), entity.getLastName(), entity.getContactNumber(),
                    entity.getAddress(), entity.getEmail(), entity.getNic(), entity.getPassportNumber(), entity.getDob(), entity.getNationalty());
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error adding guest", ex);
        }
    }

    @Override
    public boolean update(Guest entity)  {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(connection, "UPDATE guest SET first_name=?, last_name=?, address=?, passport_number=?, contact_number=? WHERE id=?",
                    entity.getFirstName(), entity.getLastName(), entity.getAddress(), entity.getPassportNumber(), entity.getContactNumber(), entity.getId());
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("There was an error updating the guest", ex);
        }
    }

    @Override
    public boolean delete( int id)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(conn,
                    "DELETE FROM guest WHERE id=?", id);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("There was an error deleting the guest: ", ex);
        }
    }

    @Override
    public Guest searchById(int id)  {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT * FROM guest WHERE id = ?", rs ->
            {
                if (rs.next()) {
                    return mapResultSetToGuest(rs);
                }
                return null;
            }, id);
            }
        catch(SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error searching the guest by id: " + id, ex);
            throw new EntityNotFoundException("There was an error finding the guest by id", ex);
        }
    }

    @Override
    public List<Guest> getAll(Map<String, String> searchParams)  {
           Map<Integer, Guest> guestsMap = new HashMap<>();
            StringBuilder query = new StringBuilder(
                    "SELECT g.*, r.reservation_number, r.status FROM guest g " +
                            "LEFT JOIN reservation r " +
                            "ON g.id = r.guest_id WHERE 1=1");

            // this part states the search params allowed
            List<Object> params = new ArrayList<>();

            // we declare the allowed parameter keys
//            Set<String> allowedParams = new HashSet<>();
//            allowedParams.add("nic");
//            allowedParams.add("passport_number");
//            allowedParams.add("registration_number");
//            allowedParams.add("status");

            if (searchParams != null && !searchParams.isEmpty()) {
                for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    // only using the allowed params
                    // status of res
                    if ("status".equals(key)) {
                        query.append(" AND r.status = ?");
                        params.add(value);
                    }
                    // otherwise we use LIKE
                    else {
                        query.append(" AND ").append(key).append(" LIKE ? ");
                        params.add("%" + value + "%");
                    }
                }
            }

            try (Connection connection = DBConnection.getInstance().getConnection()) {
                return QueryHelper.executeQuery(connection, query.toString(),
                        resultSet -> {
                            while (resultSet.next()) {
                                int id = resultSet.getInt("id");

                                Guest guest = guestsMap.get(id);

                                if (guest == null) {
                                    guest = mapResultSetToGuest(resultSet);
                                    guestsMap.put(id, guest);
                                }
                            }
                            return new ArrayList<>(guestsMap.values());  // returning the values of the map of guests

                        }, params.toArray());
            }
            catch(SQLException ex)
            {
                LOG.log(Level.INFO, "There was an error searching the guests by: " + query.toString(), ex);
                throw new DataAccessException("There was an error searching the guests", ex);
            }
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(conn,
                    "SELECT 1 FROM guest WHERE id=?", rs -> rs.next() ,primaryKey);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("There was an error finding an existing guest by primary key: " + primaryKey, ex);
        }
    }

    @Override
    public GuestHistoryDTO getGuestHistoryById(int id) {

            String sql = "SELECT r.id, r.guest_id, r.room_id, r.reservation_number, r.total_cost, r.date_of_res, " +
                    "r.checkin_date, r.checkout_date, r.num_adults, r.num_children, r.status, " +
                    "rt.name as room_type_name, " +
                    " b.total_amount, b.status AS bill_status FROM reservation r " +
                    " LEFT JOIN bill b ON  r.id = b.reservation_id " +
                    " LEFT JOIN room rm ON r.room_id = rm.room_id " +
                    " LEFT JOIN room_type rt ON rm.room_type_id = rt.room_type_id " +
                    " WHERE r.guest_id = ?";

        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, sql, resultSet ->
            {
                List<ReservationHistoryDTO> historyList = new ArrayList<>();

                while (resultSet.next()) {

                    //  int guestId = resultSet.getInt("guest_id");
                    LocalDateTime dateTime = resultSet.getTimestamp("date_of_res").toLocalDateTime();
                    ReservationDTO reservationDTO = new ReservationDTO(
                            resultSet.getInt("id"), resultSet.getInt("guest_id"), resultSet.getInt("room_id"),
                            resultSet.getString("reservation_number"), resultSet.getDouble("total_cost"),
                            dateTime,
                            resultSet.getDate("checkin_date").toLocalDate(),
                            resultSet.getDate("checkout_date").toLocalDate(),
                            resultSet.getInt("num_adults"),
                            resultSet.getInt("num_children"),
                            ReservationStatus.valueOf(resultSet.getString("status")));

                    double billTotal = resultSet.getDouble("total_amount");
                    String roomTypeName = resultSet.getString("room_type_name");

                    // payment status of a bill
                    PaymentStatus paymentStatus = null;
                    String billStatus = resultSet.getString("bill_status");

                    if (billStatus != null) {
                        paymentStatus = PaymentStatus.valueOf(billStatus);
                    }

                    // adding to the list
                    historyList.add(new ReservationHistoryDTO(reservationDTO, billTotal, paymentStatus, roomTypeName));
                }

                if (historyList.isEmpty()) {
                    return null;
                }

                return new GuestHistoryDTO(id, historyList);
        }, id);
        }
        catch (SQLException ex)
        {
            throw new EntityNotFoundException("There was an error finding the guest history for guest id: " + id, ex);
        }
    }

    private Guest mapResultSetToGuest(ResultSet resultSet) {
        try {
            Date dobDate = resultSet.getDate("dob");
            LocalDate dob = (dobDate != null) ? ((java.sql.Date) dobDate).toLocalDate() : null;

            return new Guest.GuestBuilder()
                    .id(resultSet.getInt("id"))
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .address(resultSet.getString("address"))
                    .contactNumber(resultSet.getString("contact_number"))
                    .nic(resultSet.getString("nic"))
                    .dob(dob)
                    .passportNumber(resultSet.getString("passport_number"))
                    .registrationNumber(resultSet.getString("registration_number"))
                    .email(resultSet.getString("email"))
                    .nationality(resultSet.getString("nationality"))
                    .build();
        } catch (SQLException ex) {
            throw new EntityNotFoundException("There was an error mapping the guest record.", ex);
        }
    }
}
