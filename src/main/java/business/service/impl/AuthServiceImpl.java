package business.service.impl;

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
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(AuthServiceImpl.class.getName());
  //  private final Connection connection = DBConnection.getInstance().getConnection();
    private final UserDAOImpl  userDAO = new UserDAOImpl();

    @Override
    public UserDTO login(UserCredentialDTO credentials) throws SQLException {
        if (credentials == null) {
            return null;
        }

        try {
            User user = userDAO.findByUsername( credentials.getUsername());

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
        catch (
                SQLException ex
        )
            {
            LOG.log(Level.SEVERE, "Login failed", ex);
            throw new SQLException(ex.getMessage());
            }
    }

}
