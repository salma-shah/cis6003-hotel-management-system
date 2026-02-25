package business.service;

import dto.UserDTO;
import business.service.helper.CRUDService;

import java.sql.SQLException;
import java.util.List;

public interface UserService extends CRUDService<UserDTO> {
    UserDTO findByUsername(String username) throws Exception;
    UserDTO findByEmail(String email) throws Exception;
    List<UserDTO> searchUsers(String query) throws Exception;
    boolean changePassword(String username, String password) throws Exception;
    void sendWelcomeUserEmail(UserDTO userDTO) ;
    void sendPasswordChangeEmail(UserDTO userDTO) ;
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}