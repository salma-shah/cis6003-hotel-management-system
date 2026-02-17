package persistence.dao;

import entity.User;
import persistence.dao.helper.CRUDDAO;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO extends CRUDDAO<User> {

    // these methods are for checking EXISTING results
    boolean existByUsername( String username) throws SQLException;
    boolean existByEmail(String email) throws SQLException;
    User findByUsername(String username) throws SQLException;
    User findByEmail(String email) throws SQLException;
    List<User> searchUsers(String query) throws SQLException;


}
