package persistence.dao.impl;

import constant.Role;
import db.DBConnection;
import entity.User;
import exception.db.DataAccessException;
import exception.DuplicateEntityException;
import exception.EntityNotFoundException;
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

    Logger LOG =  Logger.getLogger(UserDAOImpl.class.getName());
    // inserting a new user into the table  = working
    @Override
    public boolean add(User entity)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(conn,
                    "INSERT INTO user (username, password, first_name, last_name, contact_number, email, role, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    entity.getUsername(), entity.getPassword(), entity.getFirstName(), entity.getLastName(), entity.getContactNumber(), entity.getEmail(), entity.getRole().toString(), entity.getAddress());
        }
        catch (SQLException ex){
            throw new DataAccessException("There was an error saving the user: ", ex);
        }
    }

    @Override
    public boolean update( User entity)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(conn,
                    "UPDATE user SET first_name=?, last_name=?, contact_number=?, address=? WHERE user_id=?",
                   entity.getFirstName(), entity.getLastName(), entity.getContactNumber(), entity.getAddress(), entity.getUserId());
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("There was an error updating the user: ", ex);
        }
    }

    @Override
    public boolean delete( int id)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(conn,
                    "DELETE FROM user WHERE user_id=?", id);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("There was an error deleting the user: ", ex);
        }
    }

    @Override
    public User searchById(int id) {
        try (Connection conn = DBConnection.getInstance().getConnection()) {

            return QueryHelper.executeQuery(conn,
                    "SELECT * FROM user WHERE user_id=?",
                    rs -> {
                        if (!rs.next()) return null;
                        return mapResultSetToUser(rs);
                    },
                    id);

        } catch (SQLException ex) {
            throw new DataAccessException("Error retrieving user with ID: " + id, ex);
        }
    }

    @Override
    public List<User> getAll(Map<String, String> searchParams)  {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            List<User> users = new ArrayList<>();
            return QueryHelper.executeQuery(conn, "SELECT user_id, username, password, email, address, first_name, last_name, role, contact_number FROM user",
                    resultSet->
                    {
                        while (resultSet.next()) {
                            users.add(mapResultSetToUser(resultSet));
                        }
                        return users;
                        }
            );

        }
        catch (SQLException ex)
        {
            throw new EntityNotFoundException( "There was an error getting all the users: ", ex);
        }
    }

    @Override
    public boolean existsByPrimaryKey( int primaryKey)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(conn, "SELECT * FROM user WHERE user_id=?",
                    rs -> rs.next(), primaryKey);
        }
        catch (SQLException ex)
        {
            throw new EntityNotFoundException( "There was an error finding the user for PK: " + primaryKey, ex);
        }
    }

    // these are custom existing/finding by username and email
    @Override
    public boolean existByUsername(String username)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            // selecting the users where the same username exists
           return QueryHelper.executeQuery(conn,
                   "SELECT * FROM user WHERE username=?",
                   rs -> rs.next(), username);
           // if there are elements to retrieve, it will be returned
        }
        catch (SQLException ex)
        {
            throw new DuplicateEntityException("User with username: " + username + " already exists", ex);
        }
    }

    // same concept as above, except it is with email instead
    @Override
    public boolean existByEmail(String email)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(conn,
                    "SELECT * FROM user WHERE email=?", rs -> rs.next(), email);
        }
        catch (SQLException ex)
        {
            throw new DuplicateEntityException("User with email: " + email + " already exists", ex);
        }
    }

    // finding by username
    @Override
    public User findByUsername( String username)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(conn,
                    "SELECT * FROM user WHERE username=?", rs ->
                    {
                        if (rs.next()) {
                           return mapResultSetToUser(rs);
                        }
                        return null;

                    }, username);

        }
        catch (SQLException ex)
        {
            throw new EntityNotFoundException( "User with username: " + username + " does not exists", ex);
        }
    }

    @Override
    public User findByEmail( String email)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(conn,
                    "SELECT * FROM user WHERE email=?",
                    rs ->

                    {
                        if  (rs.next()) {
                            return mapResultSetToUser(rs);
                        }
                        return null;
                    },
                    email);

            // if result exists, it will be mapped to the User
        }
        catch (SQLException ex)
        {
            throw new EntityNotFoundException( "Error finding user with email: " + email, ex);
        }
    }

    @Override
    public List<User> searchUsers(String query)  {
            // using a list since db returns a list
            List<User> users = new ArrayList<>();

            try (Connection conn = DBConnection.getInstance().getConnection()) {
                String searchPattern = "%" + query + "%";
                return QueryHelper.executeQuery(conn,
                        "SELECT * FROM user WHERE username LIKE ? OR email LIKE ? OR CAST(user_id AS CHAR) LIKE ?",
                        rs ->
                        {
                            while (rs.next()) {
                                users.add(mapResultSetToUser(rs));
                            }
                            return users;
                        },
                        searchPattern, searchPattern, searchPattern);

            }
            catch (SQLException ex) {
                throw new DataAccessException("There was an error searching the user: ", ex);
            }
    }

    @Override
    public boolean changePassword(String username, String password)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(conn, "UPDATE user SET password=? WHERE username=?", password, username);
        }
        catch (SQLException ex)
        {
            throw new EntityNotFoundException("User with: " + username + " was not found", ex);
        }
    }

    // this is the mapping to User, based on table's column names
    private User mapResultSetToUser(ResultSet resultSet)  {
        try {
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
        catch (SQLException ex)
        {
            throw new EntityNotFoundException("There was an error mapping the user: ", ex);
        }
    }
}
