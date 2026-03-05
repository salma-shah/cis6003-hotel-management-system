package business.service;

import dto.UserCredentialDTO;
import dto.UserDTO;
import business.service.helper.CRUDService;

import java.util.List;

public interface UserService extends CRUDService<UserDTO> {
    UserDTO findByUsername(String username);
    UserDTO findByEmail(String email);
    List<UserDTO> searchUsers(String query);
    boolean changePassword(String username, String password);
    void sendWelcomeUserEmail(UserDTO userDTO) ;
    void sendPasswordChangeEmail(UserDTO userDTO) ;
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean add(UserCredentialDTO userCredentialDTO, UserDTO userDTO);
}