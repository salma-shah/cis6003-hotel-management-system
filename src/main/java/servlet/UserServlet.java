package servlet;

import constant.Role;
import dto.UserCredentialDTO;
import dto.UserDTO;
import service.AuthService;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "UserServlet", urlPatterns = "/user/*")
public class UserServlet extends HttpServlet {

    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(UserServlet.class.getName());
    private UserServiceImpl userService;

    @Override
    public void init()
    {
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // logs for debugging
        System.err.println("DO POST IS BEING HIT");
        LOG.log(Level.INFO, "Handling POST request for registration purposes.");
        LOG.info("ServletPath = " + request.getServletPath());
        LOG.info("PathInfo = " + request.getPathInfo());

        String path = request.getPathInfo();

        if (path == null) {
            path = "/";
        }

        // setting the methods based on the paths
        switch (path) {
            case "/register":
                register(request, response);
                break;
//            case "/logout":
//                logout(request, response);
//                break;
            default:
                LOG.log(Level.SEVERE, "Unsupported path: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try
        {
          LOG.log(Level.INFO, "Registering user");

            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            Role role = Role.valueOf(request.getParameter("role"));

            // ensuring the main fields are not empty
            if (username == null || email == null || password == null) {
                response.sendRedirect("auth/register?error=invalid_input");
                return;
            }

          // then we check if username or email already exists
            if (userService.findByUsername(username) != null )
            {
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/auth/register?error=username_used"));
                return;
            }

            if (userService.findByEmail(email) != null )
            {
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/auth/register?error=email_used"));
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
               response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/auth/register?success=true"));
           }
           else
            {
               LOG.log(Level.INFO, "User could not be registered successfully");
               response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/auth/register?error=system_error"));
            }
        }
        catch (Exception e)
            {
            LOG.warning(e.getMessage());
            }
    }
}
