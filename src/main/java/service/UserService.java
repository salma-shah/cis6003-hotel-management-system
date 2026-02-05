package service;

import dto.UserDTO;
import entity.User;
import service.helper.CRUDService;

public interface UserService extends CRUDService<UserDTO> {
    UserDTO findByUsername(String username) throws Exception;
    UserDTO findByEmail(String email) throws Exception;
    UserDTO getById(int id) throws Exception;
}