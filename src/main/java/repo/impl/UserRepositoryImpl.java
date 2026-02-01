package repo.impl;

import constant.Role;
import entity.User;
import repo.UserRepository;
import repo.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepositoryImpl implements UserRepository {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(UserRepositoryImpl.class.getName());

    // inserting a new user into the table
    @Override
    public boolean add(Connection conn, User entity) throws SQLException {
        try
        {
            return QueryHelper.execute(conn,
                    "INSERT INTO user (username, password, first_name, last_name, email, role) VALUES (?, ?, ?, ?, ?, ?)",
                    entity.getUsername(), entity.getPassword(), entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getRole().toString());
        }
        catch (SQLException ex){
            LOG.log(Level.SEVERE, "There was an error saving the user: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean update(Connection conn, User entity) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Connection conn, User entity) throws SQLException {
        return false;
    }

    @Override
    public User searchById(Connection conn, User entity) throws SQLException {
        return null;
    }

    @Override
    public List<User> getAll(Connection conn, User entity) throws SQLException {
        return List.of();
    }

    @Override
    public boolean existsByPk(Connection conn, User entity) throws SQLException {
        return false;
    }

    @Override
    public boolean existByUsername(Connection conn, Object... args) throws SQLException {
        try
        {
            // selecting the users where the same username exists
           ResultSet resultSet = QueryHelper.execute(conn,
                   "SELECT * FROM users WHERE username=?", args[0]);
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
    public boolean existByEmail(Connection conn, Object... args) throws SQLException {
        try
        {
            ResultSet resultSet = QueryHelper.execute(conn,
                    "SELECT * FROM users WHERE email=?", args[0]);
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
    public User findByUsername(Connection conn, Object... args) throws SQLException {
        try
        {
            ResultSet resultSet = QueryHelper.execute(conn,
                    "SELECT * FROM users WHERE username=?", args[0]);

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
                .createdAt(resultSet.getDate("created_at"))
                .updatedAt(resultSet.getDate("updated_at"))
                .deletedAt(resultSet.getDate("deleted_at"))
                .build();
    }
}
