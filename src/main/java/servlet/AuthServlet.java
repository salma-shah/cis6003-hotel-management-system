package servlet;

import dto.UserCredentialDTO;
import dto.UserDTO;
import security.PasswordManager;
import service.AuthService;
import service.impl.AuthServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "AuthServlet", urlPatterns = "/auth/*")
public class AuthServlet extends HttpServlet {

    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(AuthServlet.class.getName());
    private AuthService authService;

    @Override
    public void init() {
        this.authService = new AuthServiceImpl(); // or via factory
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // LOG.info("AuthServlet doGet hit");
//        response.sendRedirect(request.getContextPath() + "/login.jsp");

        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        if  (path.equals("/logout")) {
            logout(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // logs for debugging
//        LOG.log(Level.INFO, "Handling POST request for authentication purposes.");
//        LOG.info("ServletPath = " + request.getServletPath());
//        LOG.info("PathInfo = " + request.getPathInfo());

        String path = request.getPathInfo();

        if (path == null) {
            path = "/";
        }

       // setting the methods based on the paths
        switch (path) {
            case "/login":
                login(request, response);
                break;
//            case "/logout":
//                logout(request, response);
//                break;
            default:
                LOG.log(Level.SEVERE, "Unsupported path: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    // method for login so that we can reuse these methods in doGet and doPost both
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try
        {
            LOG.log(Level.INFO, "Login request received");

            // getting form parameters from the form
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // validating the input
            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
               response.sendRedirect(request.getContextPath() + "/auth/login?error=empty_fields");
               return;
            }

            UserCredentialDTO credentials= new UserCredentialDTO(username, password);

            // verifying password for username
            // using password manager class
            //  the service will do it

            UserDTO user =  authService.login(credentials);
            if  (user == null) {
                response.sendRedirect(request.getContextPath() + "/auth/login?error=invalid_credentials");
                return;
            }

            // if credentials are correct, then the user will be taken to dashboard
            // create session and passing use details
            HttpSession session = request.getSession();
            // System.out.println("SESSION ID (login): " + request.getSession().getId());

            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userRole", user.getRole().name());
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, "Login failed", e);
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=system_error");
        }
    }

    // logout method
    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try
        {
            LOG.log(Level.INFO, "Logout request received");

            // getting current session
            HttpSession session = request.getSession();

            // if session exists
            if (session != null) {
                // logging the users details
                String username = (String) session.getAttribute("username");

                // if username exists
                if (username != null) {
                    LOG.log(Level.INFO, "Logout from user: " + username);
                    LOG.log(Level.INFO, "Logout from session: " + session.getId());
                } else {
                    LOG.log(Level.INFO, "Logout from unidentified user");
                }

                // now destroying the session
                session.invalidate();
                LOG.log(Level.INFO, "Logout successful. Session: " + session.getId() + " was destroyed");
            }
            else
            {
                LOG.log(Level.INFO, "No active session was found to invalidate for logout request.");
            }
            }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, "There was an error during logout: ", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/auth/login?error=system_error");
        }
    }
}

