package persistence.dao;

import entity.User;
import persistence.dao.helper.CRUDDAO;

import java.util.List;

public interface UserDAO extends CRUDDAO<User> {
    boolean existByUsername( String username) ;
    boolean existByEmail(String email) ;
    User findByUsername(String username) ;
    User findByEmail(String email) ;
    List<User> searchUsers(String query) ;
    boolean changePassword(String username, String password) ;

}
