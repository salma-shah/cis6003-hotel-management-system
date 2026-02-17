package persistence.dao.impl;

import db.DBConnection;
import entity.Guest;
import persistence.dao.GuestDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
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
                    "address, email, nic, passport_number, dob) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", entity.getRegistrationNumber(),entity.getFirstName(), entity.getLastName(), entity.getContactNumber(),
                    entity.getAddress(), entity.getEmail(), entity.getNic(), entity.getPassportNumber(), entity.getDob());
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
            ResultSet resultSet = QueryHelper.execute(connection, "SELECT 1 FROM guest WHERE id = ?", id);
            if (!resultSet.next())
            {
                return null;
            }
            return mapResultSetToGuest(resultSet);
        }
        catch(SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding the guest by reg number");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public List<Guest> getAll(Map<String, String> searchParams) throws SQLException {
        return List.of();
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
                .passportNumber(resultSet.getString("passport_number"))
                .dob(resultSet.getDate("dob"))
                .registrationNumber(resultSet.getString("registration_number"))
                .email(resultSet.getString("email")).build();
    }
}
