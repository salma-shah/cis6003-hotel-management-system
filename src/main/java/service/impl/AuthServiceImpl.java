package service.impl;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import db.DBConnection;
import dto.UserCredentialDTO;
import dto.UserDTO;
import entity.User;
import mapper.UserMapper;
import security.PasswordManager;
import service.AuthService;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class AuthServiceImpl implements AuthService {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(AuthServiceImpl.class.getName());
  //  private final Connection connection = DBConnection.getInstance().getConnection();
    private final UserDAOImpl  userDAO = new UserDAOImpl();

    @Override
    public UserDTO login(UserCredentialDTO credentials) throws SQLException {
        if (credentials == null) {
            return null;
        }

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            User user = userDAO.findByUsername(connection, credentials.getUsername());

            if (user == null) {
                return null;
            }

            boolean passwordValid = PasswordManager.checkPassword(credentials.getPassword(), user.getPassword());

            if (!passwordValid) {
                throw new IllegalArgumentException("Invalid password");
            }

            // mapping entity to DTO
            return UserMapper.toUserDTO(user);
        }
    }

    @Override
    public void logout() {

    }
}
