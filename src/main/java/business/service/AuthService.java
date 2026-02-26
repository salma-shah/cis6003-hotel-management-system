package business.service;

import dto.UserCredentialDTO;
import dto.UserDTO;

public interface AuthService {
    UserDTO login(UserCredentialDTO credentialDTO);
}
