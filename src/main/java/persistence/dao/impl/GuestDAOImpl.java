package persistence.dao.impl;

import db.DBConnection;
import entity.Guest;
import persistence.dao.GuestDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuestDAOImpl implements GuestDAO {
    private static final Logger LOG = Logger.getLogger(GuestDAOImpl.class.getName());

    @Override
    public boolean findByRegistrationNumber(String registrationNumber) throws SQLException {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet = QueryHelper.execute(connection, "SELECT 1 FROM guest WHERE registration_number = ?", registrationNumber);
            return resultSet.next();
        }
        catch(SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding the guest by reg number");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public Integer findGuestIdByRegistrationNumber(String registrationNumber) throws SQLException {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet = QueryHelper.execute(connection, "SELECT id FROM guest WHERE registration_number = ?", registrationNumber);
            if (!resultSet.next())
            {
                return null;
            }
            return resultSet.getInt("id");

        }
        catch(SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding the guest by reg number");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean findByEmail(String email) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet = QueryHelper.execute(conn,
                    "SELECT 1 FROM guest WHERE email=?", email);
            return resultSet.next();
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding an existing guest by email.");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean add(Guest entity) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.execute(conn, "INSERT INTO guest (registration_number, first_name, last_name, contact_number, " +
                    "address, email, nic, passport_number, dob, nationality) VALUES (?, ?,?, ?, ?, ?, ?, ?, ?, ?)", entity.getRegistrationNumber(),entity.getFirstName(), entity.getLastName(), entity.getContactNumber(),
                    entity.getAddress(), entity.getEmail(), entity.getNic(), entity.getPassportNumber(), entity.getDob(), entity.getNationalty());
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error registering the guest: ", ex);
            throw new SQLException( ex.getMessage());
        }
    }

    @Override
    public boolean update(Guest entity) throws SQLException {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.execute(connection, "UPDATE guest SET first_name=?, last_name=?, address=?, passport_number=?, contact_number=? WHERE id=?",
                    entity.getFirstName(), entity.getLastName(), entity.getAddress(), entity.getPassportNumber(), entity.getContactNumber(), entity.getId());
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error updating the guest");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean delete( int id) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.execute(conn,
                    "DELETE FROM guest WHERE id=?", id);
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error deleting the guest: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public Guest searchById(int id) throws SQLException {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet = QueryHelper.execute(connection, "SELECT * FROM guest WHERE id = ?", id);
            if (!resultSet.next())
            {
                return null;
            }
            return mapResultSetToGuest(resultSet);
        }
        catch(SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding the guest by id");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public List<Guest> getAll(Map<String, String> searchParams) throws SQLException {
        try(Connection connection = DBConnection.getInstance().getConnection()) {

            Map<Integer, Guest> guestsMap = new HashMap<>();
            StringBuilder query = new StringBuilder(
                    "SELECT id, registration_number, first_name, last_name, address,dob, contact_number, email, passport_number, nic FROM guest WHERE 1=1"
            );


//                    ("SELECT g.*, r.reservation_number, r.status FROM guest g " +
//                    "LEFT JOIN reservation r " +
//                    "ON g.registration_number = r.registration_number");

            // this part states the search params allowed
            List<Object> params = new ArrayList<>();

            // we declare the allowed parameter keys
            Set<String> allowedParams = new HashSet<>();
            allowedParams.add("nic");
            allowedParams.add("passport_number");
            allowedParams.add("registration_number");

            if (searchParams != null && !searchParams.isEmpty()) {
                for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    // only using the allowed params
                    if (allowedParams.contains(key)) {
                        query.append(" AND ").append(key).append(" LIKE ? ");
                        params.add("%" + value + "%");
                    }
                }
            }

            try {
                ResultSet resultSet = QueryHelper.execute(connection, query.toString(), params.toArray());
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
//                    if (!guestsMap.containsKey(id)) {
//                        Guest guest = mapResultSetToGuest(resultSet);
//                    }
//                    if (guestsMap.isEmpty()) {
//                        guestsMap.put(id, null);
//                    }

                    Guest guest = guestsMap.get(id);

                    if (guest == null) {
                        guest = mapResultSetToGuest(resultSet);
                        // guest.setReservations(new ArrayList<>());  // prepare for future
                        guestsMap.put(id, guest);
                    }
                }
                return new ArrayList<>(guestsMap.values());  // returning the values of the map of guests
            }
        catch(SQLException ex)
            {
                LOG.log(Level.SEVERE, "There was an error searching the guests");
                throw new SQLException(ex.getMessage());
            }
        }
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet = QueryHelper.execute(conn,
                    "SELECT 1 FROM guest WHERE id=?", primaryKey);
            return resultSet.next();
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding an existing guest by primary key.");
            throw new SQLException(ex.getMessage());
        }
    }

    private Guest mapResultSetToGuest(ResultSet resultSet) throws SQLException {

        return new Guest.GuestBuilder().id(resultSet.getInt("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .address(resultSet.getString("address"))
                .contactNumber(resultSet.getString("contact_number"))
                .nic(resultSet.getString("nic"))
                .dob(resultSet.getDate("dob"))
                .passportNumber(resultSet.getString("passport_number"))
                .registrationNumber(resultSet.getString("registration_number"))
                .email(resultSet.getString("email")).build();
    }
}
