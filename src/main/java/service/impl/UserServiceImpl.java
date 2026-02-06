package service.impl;

import constant.Role;
import dao.UserDAO;
import dao.impl.UserDAOImpl;
import db.DBConnection;
import dto.UserCredentialDTO;
import dto.UserDTO;
import entity.User;
import mapper.UserMapper;
import security.PasswordManager;
import service.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class.getName());
    private final Connection connection = DBConnection.getInstance().getConnection();
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
            if (validateManager(userDTO)) {
                // first, generating salt and hashing password using password manager method
                String hashedPassword = PasswordManager.saltAndHashPassword(userCredentialDTO.getPassword());
                // will make user to entity using user mapper
                User userEntity = UserMapper.toUser(userDTO, userCredentialDTO, hashedPassword);
                return userDAO.add(connection, userEntity);
            }
        }

        catch (SQLException ex) {
            usernames.remove(username);
            emails.remove(email);
            throw ex;
        }
        return false;
    }

    @Override
    public boolean update(UserDTO userDTO) throws SQLException {
            // if manager is logged in
            // if (validateManager(userDTO)) {
                // fetching existing user
                User existingUser = userDAO.searchById(connection, userDTO.getUserId());

                // if it doesnt exist
                if  (existingUser == null) {
                    throw new IllegalArgumentException("User not found");
                }

                // if it exists then update the fields
                // using mapper to update
               return userDAO.update(connection, UserMapper.toUpdatedUser(existingUser, userDTO));
           // }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        // if the user for the specific PK exists, delete the user
        if (userDAO.existsByPrimaryKey(connection, id))
        {
            return userDAO.delete(connection, id);
        }
        return false;
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey) throws SQLException {
        try
        {
            return userDAO.existsByPrimaryKey(connection, primaryKey);
        }
        catch (Exception ex)
        {
            throw new SQLException(ex.getMessage());
        }
    }

    // searching methods
    @Override
    public UserDTO searchById(int id) throws SQLException {
        return UserMapper.toUserDTO(userDAO.searchById(connection, id));
    }

    @Override
    public UserDTO findByUsername(String username) throws Exception {
        try
        {
            return UserMapper.toUserDTO(userDAO.findByUsername(connection, username));
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
            return UserMapper.toUserDTO(userDAO.findByEmail(connection, email));
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public UserDTO getById(int id) throws Exception {
        try
        {
            return UserMapper.toUserDTO(userDAO.searchById(connection, id));
        }
        catch (Exception ex)
        {
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAll(Map<String, String> searchParams) throws SQLException {
        try
        {
            return UserMapper.toDTOList(userDAO.getAll(connection, searchParams));
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
