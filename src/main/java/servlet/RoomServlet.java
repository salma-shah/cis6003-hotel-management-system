package servlet;

import business.service.impl.RoomServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "RoomServlet", urlPatterns = "/room/*")
public class RoomServlet extends HttpServlet {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(RoomServlet.class.getName());
    private RoomServiceImpl roomService;

    public void init(){
        this.roomService = new RoomServiceImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // debugging
        LOG.log(Level.INFO, "DO GET is being hit.");
        String path = request.getPathInfo();

        if (path == null) {
            path = "/";
        }

        // setting the methods based on the paths
        switch (path) {
            case "/create":
                // showing registration form
                request.getRequestDispatcher("/create-rooms.jsp").forward(request, response);
                break;
            case "/all":
                request.getRequestDispatcher("/rooms.jsp").forward(request, response);
               // getAllUsers(request, response);
                break;
//            case "/get":
//                getUserDetails(request, response);
//                break;
//            case "/search":
//                searchUsers(request, response);
//                break;
            default:
                LOG.log(Level.SEVERE, "Unsupported path: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try
        {
            LOG.log(Level.INFO, "Creating room...");

        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
