package persistence.dao;

import entity.User;
import persistence.dao.helper.CRUDDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO extends CRUDDAO<User> {

    // these methods are for checking EXISTING results
    boolean existByUsername(Connection conn, String username) throws SQLException;
    boolean existByEmail(Connection conn, String email) throws SQLException;
    User findByUsername(Connection conn, String username) throws SQLException;
    User findByEmail(Connection conn, String email) throws SQLException;
    List<User> searchUsers(Connection conn, String query) throws SQLException;


}
