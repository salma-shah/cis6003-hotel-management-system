package servlet;

import business.service.impl.RoomImgServiceImpl;
import business.service.impl.RoomServiceImpl;
import constant.BeddingTypes;
import constant.RoomStatus;
import constant.RoomTypes;
import dto.RoomDTO;
import dto.RoomImgDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// this is for uploading files ; we use it for images
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5
)

@WebServlet(name = "RoomServlet", urlPatterns = "/room/*")
public class RoomServlet extends HttpServlet {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(RoomServlet.class.getName());
    private RoomServiceImpl roomService;
    private RoomImgServiceImpl roomImgService;

    public void init(){
        this.roomService = new RoomServiceImpl();
        this.roomImgService = new RoomImgServiceImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
//            case "/get":
//                getUserDetails(request, response);
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
               getAllRooms(request, response);
                break;
//            case "/get":
//                getUserDetails(request, response);
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

            int roomId = roomService.addAndReturnId(roomDTO);
            if (roomId == -1)
            {
                LOG.log(Level.SEVERE, "Unable to create room.");
            }
            LOG.log(Level.INFO, "Room has been successfully created with ID: " + roomId);

                // now we handle images
                Collection<Part> parts = request.getParts();
                List<Part> imageParts = new ArrayList<>();
                List<String> altTexts = new ArrayList<>();

                for (Part part : parts) {
                    if (part.getName().equals("roomImgs") && part.getSize() > 0) {
                        imageParts.add(part);
                    } else if (part.getName().equals("alt")) {
                        altTexts.add(new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
                    }
                }

                    // ensure we have same number of alt texts as images
                    while (altTexts.size() < imageParts.size()) {
                        altTexts.add(""); // default empty alt
                    }

                    for (int i = 0; i < imageParts.size(); i++) {
                        Part filePart = imageParts.get(i);
                        String altText = altTexts.get(i);

                        String fileName = filePart.getSubmittedFileName();

                        // this is where the images will be stored
                        // we define the location
                        String uploadPath = getServletContext().getRealPath("images/room-uploads");
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }

                        String fullPath = uploadPath + File.separator + fileName;
                        filePart.write(fullPath);
                        LOG.log(Level.INFO, "Uploaded file to: " + fullPath);

                        // now saving the image
                        // for (String imagePath : imagePaths) {
                        roomImgService.saveImg(new RoomImgDTO(0, roomId, "images/room-uploads/" + fileName, altText));
                        LOG.log(Level.INFO, "Room image has been successfully uploaded.");
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

            Map<String, String> searchParams = new HashMap<>();

            // first we check if any search parameters exist
            String floorParam = request.getParameter("floorNum");
            String statusParam = request.getParameter("status");

            // if id parameter exists, we will add id to the search params
            if (floorParam != null &&  ! floorParam.isEmpty())
            {
                int floorNum = Integer.parseInt(request.getParameter("floorNum"));
                searchParams.put("floor_num", String.valueOf(floorNum));  // search by floor number
                LOG.log(Level.INFO, "Searching for floor: " + floorParam);
            }

            if (statusParam != null && !statusParam.isEmpty()) {
                searchParams.put("status", statusParam);  // filter by status
                LOG.log(Level.INFO, "Searching for status: " + statusParam);
            }

            List<RoomDTO> rooms = roomService.getAll(searchParams);
            LOG.log(Level.INFO, "Rooms for these params are: " + searchParams);

            // this is if JS sends a search request
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    String roomJSON = new com.google.gson.Gson().toJson(rooms);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(roomJSON);
                    LOG.log(Level.INFO, "Rooms JSON sent: " + roomJSON);
                    return;
                }

            // if no search req, then just the rooms will be populated
                request.setAttribute("rooms", rooms);
                request.getRequestDispatcher("/rooms.jsp").forward(request, response);

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
