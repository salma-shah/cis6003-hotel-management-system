package servlet;

import business.service.impl.RoomServiceImpl;
import constant.BeddingTypes;
import constant.RoomStatus;
import constant.RoomTypes;
import dto.RoomDTO;
import entity.Room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
        LOG.log(Level.INFO, "DO POST is being hit.");
        String path = request.getPathInfo();

        if (path == null) {
            path = "/";
        }

        // setting the methods based on the paths
        switch (path) {
            case "/create":
                createRoom(request, response);
                break;
//            case "/all":
//                request.getRequestDispatcher("/rooms.jsp").forward(request, response);
//                // getAllUsers(request, response);
//                break;
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

    private void createRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try
        {
            LOG.log(Level.INFO, "Creating room...");
            
            // ensuring all fields are filled
            // do validation


            // if everything correct, we save the room
            RoomDTO roomDTO = new RoomDTO.RoomDTOBuilder()
                    .roomType(RoomTypes.valueOf(request.getParameter("type")))
                    .baseDescription(request.getParameter("desc"))
                    .basePricePerNight(Double.parseDouble(request.getParameter("pricePerNight")))
                    .bedding(BeddingTypes.valueOf(request.getParameter("bedding")))
                    .roomStatus(RoomStatus.valueOf(request.getParameter("status")))
                    .floorNum(Integer.parseInt(request.getParameter("floorNum")))
                    .maxOccupancy(Integer.parseInt(request.getParameter("maxOccupancy")))
                    .build();

            boolean successfulRoomCreation = roomService.add(roomDTO);
            if (successfulRoomCreation) {
                LOG.log(Level.INFO, "Room has been successfully created.");
            }
            else {
                LOG.log(Level.SEVERE, "Unable to create room.");
            }
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void getAllRooms(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            LOG.log(Level.INFO, "Getting all rooms...");
            // List<RoomDTO> rooms = roomService.getAll()

            // sample from rooms
//            request.setAttribute("users", users);
//
//            if (users.isEmpty())
//            {
//                response.sendError(HttpServletResponse.SC_NOT_FOUND);
//            }
//            else
//            {
//                LOG.log(Level.INFO, "Users found: " + users);
//                request.getRequestDispatcher("/user-accounts.jsp").forward(request, response);
//            }
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    // deleting a room method
    private void deleteRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.log(Level.INFO, "Deleting room...");
        try
        {
            String id = request.getParameter("roomId");
            int roomId = Integer.parseInt(id);
            if (roomId == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            boolean isDeleted = roomService.delete(roomId);
            if (isDeleted)
            {
                LOG.log(Level.INFO, "Room ID:" + roomId + " was deleted successfully");
                request.getRequestDispatcher("/user-accounts.jsp").forward(request, response);
            }
            else
            {
                LOG.log(Level.INFO, "User ID:" + roomId + " was not deleted.");
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting user:" +  ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
