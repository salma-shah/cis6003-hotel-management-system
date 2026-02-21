package servlet;
import business.service.RoomService;
import business.service.impl.RoomServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import constant.RoomStatus;
import dto.RoomDTO;
import dto.RoomTypeDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// this is for uploading files ; we use it for images
//@MultipartConfig(
//        fileSizeThreshold = 1024 * 1024,
//        maxFileSize = 1024 * 1024 * 5,
//        maxRequestSize = 1024 * 1024 * 5
//)

@WebServlet(name = "RoomServlet", urlPatterns = "/room/*")
public class RoomServlet extends HttpServlet {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(RoomServlet.class.getName());
    private RoomService roomService;
//    private RoomImgService roomImgService;
//    private AmenityService amenityService;

    public void init() {
        this.roomService = new RoomServiceImpl();
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
        try {
            LOG.log(Level.INFO, "Creating room...");

            // ensuring all fields are filled
            // do validation


            // if everything correct, we save the room
            // setting room TYPE first
            RoomTypeDTO roomTypeDTO = new RoomTypeDTO.Builder().roomTypeId(Integer.parseInt(request.getParameter("roomTypeId"))).build();

            RoomDTO roomDTO = new RoomDTO.RoomDTOBuilder()
                    .floorNum(Integer.parseInt(request.getParameter("floorNum")))
                    .roomNum((request.getParameter("roomNum")))
                    .roomStatus(RoomStatus.valueOf(request.getParameter("status")))
                    .roomType(roomTypeDTO)
                    .build();

            boolean createdRoom = roomService.add(roomDTO);
            if (createdRoom) {
                LOG.log(Level.INFO, "Room created successfully!");
            }
            else {
                LOG.log(Level.SEVERE, "Failed to create room!");
            }

            // now we handle images
//            Collection<Part> parts = request.getParts();
//            List<Part> imageParts = new ArrayList<>();
//            List<String> altTexts = new ArrayList<>();
//
//            for (Part part : parts) {
//                if (part.getName().equals("roomImgs") && part.getSize() > 0) {
//                    imageParts.add(part);
//                } else if (part.getName().equals("alt")) {
//                    altTexts.add(new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
//                }
//            }
//
//            // ensure we have same number of alt texts as images
//            while (altTexts.size() < imageParts.size()) {
//                altTexts.add(""); // default empty alt
//            }
//
//            for (int i = 0; i < imageParts.size(); i++) {
//                Part filePart = imageParts.get(i);
//                String altText = altTexts.get(i);
//
//                String fileName = filePart.getSubmittedFileName();
//
//                // this is where the images will be stored
//                // we define the location
//                String uploadPath = getServletContext().getRealPath("images/room-uploads");
//                File uploadDir = new File(uploadPath);
//                if (!uploadDir.exists()) {
//                    uploadDir.mkdirs();
//                }
//
//                String fullPath = uploadPath + File.separator + fileName;
//                filePart.write(fullPath);
//                LOG.log(Level.INFO, "Uploaded file to: " + fullPath);
//
//                // now saving the image
//                // for (String imagePath : imagePaths) {
//                roomImgService.saveImg(new RoomImgDTO(0, roomId, "images/room-uploads/" + fileName, altText));
//                LOG.log(Level.INFO, "Room image has been successfully uploaded.");
//
//
//                // now we add the amenities
//                String[] amenities = request.getParameterValues("amenities");
//                LOG.log(Level.INFO, "Amenities are: " + Arrays.toString(amenities));
//                if (amenities != null) {
//                    for (String amenity : amenities) {
//                        if (amenity == null) {
//                            continue;
//                        }
//                        try {
//                            int amenityId = Integer.parseInt(amenity.trim());
//                            boolean addedAmenity = amenityService.addAmenityToRoom(roomId, amenityId);
//
//                            if (!addedAmenity) {
//                                LOG.log(Level.SEVERE, "Amenity for ID: " + amenityId + "could not be added.");
//                            }
//
//                            LOG.log(Level.INFO, "Amenity has been successfully added: " + amenityId);
//                        } catch (NumberFormatException nx) {
//                            LOG.log(Level.SEVERE, "Invalid amenity parameters.");
//                        }
//                    }
//                }
//            }
        } catch (Exception ex) {
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
            String statusParam = request.getParameter("statusFilter");
            String beddingParam = request.getParameter("beddingFilter");
            String guestsAdultsParam = request.getParameter("guestsAdultsFilter");
            String guestsChildsParam = request.getParameter("guestsChildrenFilter");
            String[] amenitiesParam = request.getParameterValues("amenitiesFilter");
            String roomTypeParam  = request.getParameter("roomTypeFilter");
            String checkInDate = request.getParameter("checkInDate");
            String checkOutDate = request.getParameter("checkOutDate");


            // if floor num parameter exists, we will add it to the search params
            if (floorParam != null && !floorParam.isEmpty()) {
                int floorNum = Integer.parseInt(request.getParameter("floorNum"));
                searchParams.put("floor_num", String.valueOf(floorNum));  // search by floor number
                LOG.log(Level.INFO, "Searching for floor: " + floorParam);
            }

            if (statusParam != null && !statusParam.isEmpty()) {
                searchParams.put("status", statusParam);  // filter by status
                LOG.log(Level.INFO, "Searching for status: " + statusParam);
            }

            if (roomTypeParam != null && !roomTypeParam.isEmpty()) {
                searchParams.put("name", roomTypeParam);  // filter by room type
                LOG.log(Level.INFO, "Searching for room type: " + roomTypeParam);
            }

            if (beddingParam != null && !beddingParam.isEmpty()) {
                searchParams.put("bedding", beddingParam);  // filter by bedding type
                LOG.log(Level.INFO, "Searching for bedding type: " + beddingParam);
            }

            if (amenitiesParam != null && amenitiesParam.length > 0) {
                String amenitiesCSV =  String.join(",", amenitiesParam);
                    searchParams.put("amenityId", amenitiesCSV);
                    LOG.log(Level.INFO, "Searching for amenities: " + amenitiesCSV);
            }

            // checkin checkout params
            if(checkInDate != null) {
                searchParams.put("check_in", String.valueOf(checkInDate));
                LOG.log(Level.INFO, "Searching for check_in: " + checkInDate);
            }

            if(checkOutDate != null) {
                searchParams.put("check_out", String.valueOf(checkOutDate));
                LOG.log(Level.INFO, "Searching for check_out: " + checkOutDate);
            }

            List<RoomDTO> rooms = roomService.getAll(searchParams);
            LOG.log(Level.INFO, "Rooms for these params are: " + searchParams);

            // since filtering for max occupancy involves using another service layer logic
            int adults = ((guestsAdultsParam != null) && !guestsAdultsParam.isEmpty()) ? Integer.parseInt(guestsAdultsParam) : 0;
            int children = ((guestsChildsParam != null && !guestsChildsParam.isEmpty()) ? Integer.parseInt(guestsChildsParam) : 0);

            if (adults > 0 || children > 0) {

                // now we filter the rooms by max occupancy method
                rooms = rooms.stream()
                        .filter(room -> {
                            try {
                                LOG.log(Level.INFO, "Searching for room with guests: " + adults + " and " + children);
                                return roomService.isRoomEligible(room, adults, children);
                            } catch (SQLException ex) {
                                LOG.log(Level.INFO, "Eligible rooms could not be checked: " + ex);
                                return false;
                            }
                        })
                        .toList();
            }


//            // converting dob into being serializable
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd").format(src)))
                    .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src.toString()))
                    .create();

            // this is if JS sends a search request
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                String roomJSON = gson.toJson(rooms);
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
        catch(Exception ex)
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
