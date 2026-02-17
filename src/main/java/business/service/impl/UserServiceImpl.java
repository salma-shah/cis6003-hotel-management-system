package business.service.impl;

import constant.Role;
import persistence.dao.UserDAO;
import persistence.dao.impl.UserDAOImpl;
import dto.UserCredentialDTO;
import dto.UserDTO;
import entity.User;
import mapper.UserMapper;
import security.PasswordManager;
import business.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class.getName());
    // private final Connection connection = DBConnection.getConnection();
    private final UserDAO userDAO;

    // setting up hash sets for usernames and emails
    // preloading them
    private final Set<String> usernames = ConcurrentHashMap.newKeySet();
    private final Set<String> emails = ConcurrentHashMap.newKeySet();

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
        preloadUniqueFields();  // we preload the unique usernames and fields so nothing new pops up after tomcat server is restarted ecery time
    }

    private void preloadUniqueFields() {
        List<UserDTO> userDTOs = new ArrayList<>();

        // lambda function
        userDTOs.forEach(userDTO -> {
            usernames.add(userDTO.getUsername());
            emails.add(userDTO.getEmail());
        });
    }

    public boolean add(UserCredentialDTO userCredentialDTO, UserDTO userDTO) throws SQLException {
       // checking that username or email is unique
        String username = userCredentialDTO.getUsername();
        String email = userDTO.getEmail();

        if (!usernames.add(username)) {
            throw new IllegalArgumentException("Duplicate username");
        }

        if (!emails.add(email)) {
            throw new IllegalArgumentException("Duplicate email");
        }

        try {
            // if manager is logged in
         //   if (validateManager(userDTO)) {
                // first, generating salt and hashing password using password manager method
                String hashedPassword = PasswordManager.saltAndHashPassword(userCredentialDTO.getPassword());
                // will make user to entity using user mapper
                User userEntity = UserMapper.toUser(userDTO, userCredentialDTO, hashedPassword);
                return userDAO.add( userEntity);
            }
       // }
        catch (SQLException ex) {
            usernames.remove(username);
            emails.remove(email);
            throw ex;
        }
    }

    @Override
    public boolean update(UserDTO userDTO) throws SQLException {
        try {
            // if manager is logged in
            // if (validateManager(userDTO)) {
            // fetching existing user
            User existingUser = userDAO.searchById( userDTO.getUserId());

            // if it doesnt exist
            if (existingUser == null) {
                throw new IllegalArgumentException("User not found");
            }
            // if it exists then update the fields
            // using mapper to update
            return userDAO.update( UserMapper.toUpdatedUser(existingUser, userDTO));
        }
        catch (SQLException ex) {
            LOG.log(Level.INFO, "Something went wrong with updating the user: " + ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try  {
        // if the user for the specific PK exists, delete the user
        if (userDAO.existsByPrimaryKey( id))
        {
            return userDAO.delete( id);
        }}
        catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }

        return false;
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey) throws SQLException {
        try
        {
            return userDAO.existsByPrimaryKey( primaryKey);
        }
        catch (Exception ex)
        {
            throw new SQLException(ex.getMessage());
        }
    }

    // searching methods
    @Override
    public UserDTO searchById(int id) throws SQLException {
        return UserMapper.toUserDTO(userDAO.searchById( id));
    }

    @Override
    public UserDTO findByUsername(String username) throws Exception {
        try
        {
            return UserMapper.toUserDTO(userDAO.findByUsername( username));
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public UserDTO findByEmail(String email) throws Exception {
        try
        {
            return UserMapper.toUserDTO(userDAO.findByEmail( email));
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public List<UserDTO> searchUsers(String query) throws Exception {
        return UserMapper.toDTOList(userDAO.searchUsers( query));
    }

    @Override
    public List<UserDTO> getAll(Map<String, String> searchParams) throws SQLException {
        try
        {
            return UserMapper.toDTOList(userDAO.getAll( searchParams));
        }
        catch (Exception ex)
        {
            throw new SQLException(ex.getMessage());
        }
    }

    // method to validate that user is a manager logged in
    private boolean validateManager(UserDTO manager) throws SQLException {
        return manager.getRole() == Role.Manager && manager.getUsername() != null;
    }
}
