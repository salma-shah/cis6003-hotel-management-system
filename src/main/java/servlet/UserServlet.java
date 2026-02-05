package servlet;

import constant.Role;
import dto.UserCredentialDTO;
import dto.UserDTO;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        System.err.println("DO POST IS BEING HIT");
        LOG.log(Level.INFO, "Handling POST request for registration purposes.");
        LOG.info("ServletPath = " + request.getServletPath());
        LOG.info("PathInfo = " + request.getPathInfo());
        LOG.log(Level.INFO, "Context path = " + request.getContextPath());

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
//            case "/register":
//            // showing registration form
//            request.getRequestDispatcher("/register.jsp").forward(request, response);
//            break;
            case "/all":
                getAllUsers(request, response);
                break;
            default:
                LOG.log(Level.SEVERE, "Unsupported path: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try
        {
          LOG.log(Level.INFO, "Registering user");

            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            Role role = Role.valueOf(request.getParameter("role"));
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String address = request.getParameter("address");
            String contactNumber = request.getParameter("contactNumber");

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
           if (successRegistration)
           {
               LOG.log(Level.INFO, "User registered successfully");
               response.sendRedirect(request.getContextPath() + "/user/register?success=true");
           }
           else
            {
               LOG.log(Level.INFO, "User could not be registered successfully");
               response.sendRedirect(request.getContextPath() + "/user/register?error=system_error");
            }
        }
        catch (Exception e)
            {
            LOG.warning(e.getMessage());
            }
    }

    private void searchUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

}