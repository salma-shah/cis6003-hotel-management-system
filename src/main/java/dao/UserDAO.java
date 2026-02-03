package dao;

import entity.User;
import dao.helper.CRUDDAO;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserDAO extends CRUDDAO<User> {

    // these methods are for checking EXISTING results
    boolean existByUsername(Connection conn, String username) throws SQLException;
    boolean existByEmail(Connection conn, String email) throws SQLException;
    User findByUsername(Connection conn, String username) throws SQLException;
    User findByEmail(Connection conn, String email) throws SQLException;


}
