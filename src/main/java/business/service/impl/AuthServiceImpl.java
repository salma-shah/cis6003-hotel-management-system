package business.service.impl;

import exception.user.InvalidUserCredentialsException;
import exception.user.UserNotFoundException;
import persistence.dao.impl.UserDAOImpl;
import dto.UserCredentialDTO;
import dto.UserDTO;
import entity.User;
import mapper.UserMapper;
import security.PasswordManager;
import business.service.AuthService;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthServiceImpl implements AuthService {
    private final UserDAOImpl  userDAO = new UserDAOImpl();

    @Override
    public UserDTO login(UserCredentialDTO credentials) {
        if (credentials == null) {
            throw new IllegalArgumentException("Username and password are required");
        }
            User user = userDAO.findByUsername( credentials.getUsername());
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            boolean passwordValid = PasswordManager.checkPassword(credentials.getPassword(), user.getPassword());

            if (!passwordValid) {
                throw new InvalidUserCredentialsException("Invalid password or username.");
            }

            // mapping entity to DTO
            return UserMapper.toUserDTO(user);
    }

}
