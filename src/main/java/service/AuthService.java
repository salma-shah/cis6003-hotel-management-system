package service;

import dto.UserCredentialDTO;
import dto.UserDTO;

import java.sql.SQLException;

public interface AuthService {
    UserDTO login(UserCredentialDTO credentialDTO) throws SQLException;
}
