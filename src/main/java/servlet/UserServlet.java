package servlet;

import constant.Role;
import dto.UserCredentialDTO;
import dto.UserDTO;
import mail.EmailBase;
import mail.EmailUtility;
import mail.factory.EmailCreator;
import mail.factory.impl.WelcomeEmailCreator;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "UserServlet", urlPatterns = "/user/*")
public class UserServlet extends HttpServlet {

    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(UserServlet.class.getName());
    private UserServiceImpl userService;

    @Override
    public void init() {
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // logs for debugging
//        System.err.println("DO POST IS BEING HIT");
//        LOG.log(Level.INFO, "Handling POST request for registration purposes.");
//        LOG.info("ServletPath = " + request.getServletPath());
//        LOG.info("PathInfo = " + request.getPathInfo());
//        LOG.log(Level.INFO, "Context path = " + request.getContextPath());

        String path = request.getPathInfo();

        if (path == null) {
            path = "/";
        }

        // setting the methods based on the paths
        switch (path) {
            case "/register":   // this is for registering users
                register(request, response);
                break;
            case "/delete":   // this is for deleting a user ; since html forms dont have DELETE,
                    deleteUser(request, response);   // we access it using POST method
                request.getRequestDispatcher("/user-accounts.jsp").forward(request, response);
                break;
            case "/update":
                updateUser(request, response);
                break;
            default:
                LOG.log(Level.SEVERE, "Unsupported path: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // logs for debugging
        // System.err.println("DO GET IS BEING HIT");
        // request.getRequestDispatcher("/register.jsp").forward(request, response);

        String path = request.getPathInfo();

        if (path == null) {
            path = "/";
        }

        // setting the methods based on the paths
        switch (path) {
             case "/register":
             // showing registration form
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            break;
            case "/all":
                getAllUsers(request, response);
                break;
            case "/get":
                getUserDetails(request, response);
                break;
            case "/search":
                searchUsers(request, response);
                break;
            default:
                LOG.log(Level.SEVERE, "Unsupported path: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // crating  a new user account
    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try
        {
          LOG.log(Level.INFO, "Registering user");

            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            Role role = Role.valueOf(request.getParameter("role"));

          //   ensuring the main fields are not empty
            if (username == null || email == null || password == null || username.isEmpty() ||  email.isEmpty() || password.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/user/register?error=invalid_input");
                return;
            }

          // then we check if username or email already exists
            if (userService.findByUsername(username) != null )
            {
                response.sendRedirect(request.getContextPath() + "/user/register?error=username_used");
                return;
            }

            if (userService.findByEmail(email) != null )
            {
                response.sendRedirect(request.getContextPath() + "/user/register?error=email_used");
                return;
            }

            // if everything is correct, then new user will be registered
            UserDTO userDTO = new UserDTO.UserDTOBuilder().username(request.getParameter("username"))
                    .email(request.getParameter("email"))
                    .firstName(request.getParameter("firstName"))
                    .lastName(request.getParameter("lastName"))
                    .address(request.getParameter("address"))
                    .contactNumber(request.getParameter("contactNumber"))
                    .role(role)
                    .build();

            UserCredentialDTO userCredentialDTO = new UserCredentialDTO(username, password);

            // now the service will do the registering
           boolean successRegistration = userService.add(userCredentialDTO, userDTO);

           // successful account registered
           if (successRegistration)
           {
               LOG.log(Level.INFO, "User registered successfully.");

               // sending the email
               // using the creator method for welcoming user email
               EmailCreator emailCreator = new WelcomeEmailCreator((userDTO.getFirstName() + " " + userDTO.getLastName()), userDTO.getEmail(), userDTO.getUsername(), userCredentialDTO.getPassword());

               // then using the base interface
               EmailBase emailBase = emailCreator.createEmail();

               // then finally the send mail utility
               EmailUtility.sendMail(emailBase.getReceiver(), emailBase.getSubject(), emailBase.getBody());

               LOG.log(Level.INFO, "Email sent successfully.");

               // response.sendRedirect(request.getContextPath() + "/user/register?success=true");
           }
           // failed to do so
           else
            {
               LOG.log(Level.INFO, "User could not be registered successfully nor was email sent.");
               response.sendRedirect(request.getContextPath() + "/user/register?error=system_error");
            }
        }
        catch (Exception e)
            {
            LOG.warning(e.getMessage());
            }
    }

    private void getAllUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.log(Level.INFO, "Getting all users");

        try {

            List<UserDTO> users = userService.getAll(null);
            request.setAttribute("users", users);

            if (users.isEmpty())
            {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            else
            {
                LOG.log(Level.INFO, "Users found: " + users);
                request.getRequestDispatcher("/user-accounts.jsp").forward(request, response);
            }
        }
        catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error fetching users:" +  ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // passing user details to the user acc modal
    private void getUserDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.log(Level.INFO, "Getting user details method reached...");

        String id = request.getParameter("id");
        LOG.log(Level.INFO, "User ID passed is: " + id);

        // turning the string into an integer
        int userId = Integer.parseInt(id);
        LOG.log(Level.INFO, "Getting user details for user ID :" + userId);

        try {
            if (userId == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The userID is missing.");
                return;
            }

            // if user id exists, details for that user id will be passed as JSON to frontend
            UserDTO user = userService.searchById(userId);

            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            // converting user to JSON for javascript to recognize
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // using Gson to serialize
            String userJson = new com.google.gson.Gson().toJson(user);
            response.getWriter().write(userJson);
            LOG.log(Level.INFO, "User JSON sent: " + userJson);

        }
        catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error fetching user details: " + ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }

    // updating user account's details
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.log(Level.INFO, "Updating user ID : " + request.getParameter("userId"));
        String id = (request.getParameter("userId"));
        int userId = Integer.parseInt(id);

        try
        {
            UserDTO user = new UserDTO.UserDTOBuilder().userId(userId)
                    .firstName(request.getParameter("firstName"))
                    .lastName(request.getParameter("lastName"))
                    .address(request.getParameter("address"))
                    .contactNumber(request.getParameter("contactNumber"))
                    .build();

            // if user exists, fields will be updated
            boolean isUpdated = userService.update(user);
            if (isUpdated) {
                LOG.log(Level.INFO, "User with ID: " + userId + "was updated successfully");
            //   request.getRequestDispatcher("/user-accounts.jsp").forward(request, response);
            }
            else
            {
                LOG.log(Level.INFO, "User details of userID: " + userId + "was not updated");
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error updating the user details: " + ex);
        }
    }

    // deleting user method
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.log(Level.INFO, "Deleting user");
        try
        {
            String id = request.getParameter("userId");
            int userId = Integer.parseInt(id);
            if (userId == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            boolean isDeleted = userService.delete(userId);
            if (isDeleted)
            {
                LOG.log(Level.INFO, "User ID:" + userId + " was deleted successfully");
                request.getRequestDispatcher("/user-accounts.jsp").forward(request, response);
            }
            else
            {
                LOG.log(Level.INFO, "User ID:" + userId + " was not deleted.");
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting user:" +  ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void searchUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.log(Level.INFO, "Searching users...");

        try
        {
            List<UserDTO> users = userService.searchUsers(request.getParameter("q"));
            if (users == null) {
                users = Collections.emptyList();
            }
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String userJson = new com.google.gson.Gson().toJson(users);
                response.getWriter().write(userJson);
                LOG.log(Level.INFO, "Users JSON sent: " + userJson);
        }

        catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error searching users: " + ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}