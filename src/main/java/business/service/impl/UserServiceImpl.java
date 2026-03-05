package business.service.impl;

import business.service.UserService;
import constant.Role;
import dto.UserCredentialDTO;
import dto.UserDTO;
import entity.User;
import exception.service.DuplicateEmailException;
import exception.user.DuplicateUsernameException;
import exception.user.UnauthorizedRoleException;
import exception.user.UserNotFoundException;
import mail.EmailBase;
import mail.EmailUtility;
import mail.factory.EmailFactory;
import mail.factory.impl.PasswordChangeEmailFactory;
import mail.factory.impl.WelcomeEmailFactory;
import mapper.UserMapper;
import persistence.dao.UserDAO;
import persistence.dao.impl.UserDAOImpl;
import security.PasswordManager;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserDAO userDAO;

    // setting up sets for usernames and emails
    // preloading them
    private final Set<String> usernames = ConcurrentHashMap.newKeySet();
    private final Set<String> emails = ConcurrentHashMap.newKeySet();

    public UserServiceImpl() {
            this.userDAO = new UserDAOImpl();
            preloadUniqueFields();  // we preload the unique usernames and fields so nothing new pops up after tomcat server is restarted every time
        }

        private void preloadUniqueFields() {
            List<UserDTO> userDTOs = getAll(null);

            // lambda function
            userDTOs.forEach(userDTO -> {
                usernames.add(userDTO.getUsername());
                emails.add(userDTO.getEmail());
            });
        }

        public boolean add(UserCredentialDTO userCredentialDTO, UserDTO userDTO)  {
            // checking that username or email is unique
            String username = userCredentialDTO.getUsername();
            String email = userDTO.getEmail();

            if (!usernames.add(username)) {
                throw new DuplicateUsernameException("Username" + username + " already exists");
            }

            if (!emails.add(email)) {
                throw new DuplicateEmailException("Email " + email + " already exists");
            }

            // if manager is logged in
           // validateManager(userDTO);

            // first, generating salt and hashing password using password manager method
            String hashedPassword = PasswordManager.saltAndHashPassword(userCredentialDTO.getPassword());
            // will make user to entity using user mapper
            User userEntity = UserMapper.toUser(userDTO, userCredentialDTO, hashedPassword);
            boolean savedUser = userDAO.add(userEntity);
            if (!savedUser) {
                usernames.remove(username);
                emails.remove(email);
                return false;
            }

            // sending email
            // sending the email
            // using the creator method for welcoming user email
            EmailFactory emailFactory = new WelcomeEmailFactory((userDTO.getFirstName() + " " + userDTO.getLastName()), userDTO.getEmail(), userDTO.getUsername());

            // then using the base interface
            EmailBase emailBase = emailFactory.createEmail();

            // then finally the send mail utility
            EmailUtility.sendMail(emailBase.getReceiver(), emailBase.getSubject(), emailBase.getBody());
            return true;
        }

        @Override
        public boolean update(UserDTO userDTO)  {

            // fetching existing user
            User existingUser = userDAO.searchById(userDTO.getUserId());

            // if it doesnt exist
            if (existingUser == null) {
                throw new UserNotFoundException("User not found");
            }
            // if it exists then update the fields
            // using mapper to update
            return userDAO.update( UserMapper.toUpdatedUser(existingUser, userDTO));
        }

        @Override
        public boolean delete(int id)  {

        if (id <= 0) { throw new IllegalArgumentException("User ID is invalid"); }

        // if the user for the specific PK exists, delete the user
            if (!userDAO.existsByPrimaryKey(id)) {
                throw new UserNotFoundException("User not found");
            }
            return userDAO.delete(id);
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey)  {
        if (primaryKey <= 0)
        {
            throw new IllegalArgumentException("User ID is invalid");
        }

        return userDAO.existsByPrimaryKey(primaryKey);
    }

    // searching methods
    @Override
    public UserDTO searchById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("User ID is invalid");
        }
        User user = userDAO.searchById(id);

        if (user == null) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }

        return UserMapper.toUserDTO(user);
    }

    @Override
    public UserDTO findByUsername(String username) {
        if (username == null)
        {
            throw new IllegalArgumentException("Username is invalid");
        }

        User user = userDAO.findByUsername(username);
        if (user == null)
        {
            throw new UserNotFoundException("User not found");
        }
        return UserMapper.toUserDTO(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        if (email == null)
        {
            throw new IllegalArgumentException("Email is invalid");
        }

        User user = userDAO.findByEmail(email);
        if (user == null)
        {
            throw new UserNotFoundException("User not found");
        }
        return UserMapper.toUserDTO(user);
    }

    @Override
    public List<UserDTO> searchUsers(String query) {
        return UserMapper.toDTOList(userDAO.searchUsers(query));
    }

    @Override
    public List<UserDTO> getAll(Map<String, String> searchParams)  {
        return UserMapper.toDTOList(userDAO.getAll( searchParams));
    }

    @Override
    public boolean changePassword(String username, String password) {

        if (!userDAO.existByUsername(username)) {
            throw new UserNotFoundException("User for username: " + username + " not found");
        }

        // we need to hash the password before we store it
        String hashedPassword = PasswordManager.saltAndHashPassword(password);
        userDAO.changePassword(username, hashedPassword);
        UserDTO userDTO = UserMapper.toUserDTO(userDAO.findByUsername(username));
        sendPasswordChangeEmail(userDTO);
        return true;
    }

    @Override
    public void sendWelcomeUserEmail(UserDTO userDTO)  {
        // sending email
        // sending the email
        // using the creator method for welcoming user email
        EmailFactory emailFactory = new WelcomeEmailFactory((userDTO.getFirstName() + " " + userDTO.getLastName()),
                userDTO.getEmail(), userDTO.getUsername());

        // then using the base interface
        EmailBase emailBase = emailFactory.createEmail();

        // then finally the send mail utility
        EmailUtility.sendMail(emailBase.getReceiver(), emailBase.getSubject(), emailBase.getBody());
    }

    @Override
    public void sendPasswordChangeEmail(UserDTO userDTO)  {
        // sending email
        // sending the email
        // using the creator method for welcoming user email
        EmailFactory emailFactory = new PasswordChangeEmailFactory((userDTO.getFirstName() + " " + userDTO.getLastName()),
                userDTO.getEmail());

        // then using the base interface
        EmailBase emailBase = emailFactory.createEmail();

        // then finally the send mail utility
        EmailUtility.sendMail(emailBase.getReceiver(), emailBase.getSubject(), emailBase.getBody());
    }

    @Override
    public boolean existsByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is invalid");
        }
        return userDAO.existByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is invalid");
        }
        return userDAO.existByEmail(email);
    }

    // method to validate that user is a manager logged in
    private void validateManager(UserDTO manager) {
        if (manager == null ||
                manager.getRole() != Role.Manager ||
                manager.getUsername() == null) {
            throw new UnauthorizedRoleException("Only managers can create user accounts");
        }
    }
}
