package service;

import dto.UserDTO;
import entity.User;
import service.helper.CRUDService;

import java.util.List;

public interface UserService extends CRUDService<UserDTO> {
    UserDTO findByUsername(String username) throws Exception;
    UserDTO findByEmail(String email) throws Exception;
    List<UserDTO> searchUsers(String query) throws Exception;
}