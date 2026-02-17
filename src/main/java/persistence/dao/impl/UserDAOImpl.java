package persistence.dao.impl;

import constant.Role;
import db.DBConnection;
import entity.User;
import persistence.dao.UserDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAOImpl implements UserDAO {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(UserDAOImpl.class.getName());

    // inserting a new user into the table  = working
    @Override
    public boolean add(User entity) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.execute(conn,
                    "INSERT INTO user (username, password, first_name, last_name, contact_number, email, role, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    entity.getUsername(), entity.getPassword(), entity.getFirstName(), entity.getLastName(), entity.getContactNumber(), entity.getEmail(), entity.getRole().toString(), entity.getAddress());
        }
        catch (SQLException ex){
            LOG.log(Level.SEVERE, "There was an error saving the user: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean update( User entity) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.execute(conn,
                    "UPDATE user SET first_name=?, last_name=?, contact_number=?, address=? WHERE user_id=?",
                   entity.getFirstName(), entity.getLastName(), entity.getContactNumber(), entity.getAddress(), entity.getUserId());
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error updating the user: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean delete( int id) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.execute(conn,
                    "DELETE FROM user WHERE user_id=?", id);
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error deleting the user: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public User searchById( int id) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet =  QueryHelper.execute(conn, "SELECT * FROM user WHERE user_id=?", id);
            if (!  resultSet.next())
            {
                return null;

            }
            return mapResultSetToUser(resultSet);
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error searching the user: ", ex);
            throw new SQLException(ex.getMessage());
        }

    }

    @Override
    public List<User> getAll(Map<String, String> searchParams) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            ResultSet resultSet = QueryHelper.execute(conn, "SELECT user_id, username, password, email, address, first_name, last_name, role, contact_number FROM user");
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
            return users;
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error getting all the users: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean existsByPrimaryKey( int primaryKey) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet =  QueryHelper.execute(conn, "SELECT * FROM user WHERE user_id=?", primaryKey);
            return resultSet.next();
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding the user for the PK: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    // these are custom existing/finding by username and email
    @Override
    public boolean existByUsername(String username) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            // selecting the users where the same username exists
           ResultSet resultSet = QueryHelper.execute(conn,
                   "SELECT * FROM user WHERE username=?", username);
           return resultSet.next();  // if there are elements to retrieve, it will be returned
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding an existing user by username: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    // same concept as above, except it is with email instead
    @Override
    public boolean existByEmail(String email) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet = QueryHelper.execute(conn,
                    "SELECT * FROM user WHERE email=?", email);
            return resultSet.next();
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding an existing user by email: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    // finding by username
    @Override
    public User findByUsername( String username) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet = QueryHelper.execute(conn,
                    "SELECT * FROM user WHERE username=?", username);

            // if result exists, it will be mapped to the User
            if  (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            return null;
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding the username: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public User findByEmail( String email) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet = QueryHelper.execute(conn,
                    "SELECT * FROM user WHERE email=?", email);

            // if result exists, it will be mapped to the User
            if  (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            else {return null;}
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding the email: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public List<User> searchUsers(String query) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            // using a list since db returns a list
            List<User> users = new ArrayList<>();

            try {
                String searchPattern = "%" + query + "%";
                ResultSet resultSet = QueryHelper.execute(conn,
                        "SELECT * FROM user WHERE username LIKE ? OR email LIKE ? OR CAST(user_id AS CHAR) LIKE ?", searchPattern, searchPattern, searchPattern);

                // if result exists, it will be mapped to the User
                while (resultSet.next()) {
                    users.add(mapResultSetToUser(resultSet));
                }
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "There was an error searching the user: ", ex);
                throw new SQLException(ex.getMessage());
            }
            LOG.log(Level.INFO, "There are " + users.size() + " users in the database");
            return users;
        }
    }

    // this is the mapping to User, based on table's column names
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        return new User.UserBuilder()
                .userId(resultSet.getString("user_id"))
                .password(resultSet.getString("password"))
                .email(resultSet.getString("email"))
                .username(resultSet.getString("username"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .contactNumber(resultSet.getString("contact_number"))
                .role(Role.valueOf(resultSet.getString("role")))
                .address(resultSet.getString("address"))
                .build();
    }
}
