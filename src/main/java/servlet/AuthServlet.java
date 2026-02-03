package servlet;

import dto.UserCredentialDTO;
import dto.UserDTO;
import service.AuthService;
import service.impl.AuthServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "AuthServlet", urlPatterns = "/auth")
public class AuthServlet extends HttpServlet {

    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(AuthServlet.class.getName());
    private AuthService authService;

    @Override
    public void init() {
        this.authService = new AuthServiceImpl(); // or via factory
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try {
            UserCredentialDTO credentials =
                    new UserCredentialDTO(
                            request.getParameter("username"),
                            request.getParameter("password")
                    );

            UserDTO user = authService.login(credentials);

            if (user == null) {
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

//            LOG.info("Context path = " + request.getContextPath());
            request.getSession().setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
