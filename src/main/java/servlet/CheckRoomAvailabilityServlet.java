package servlet;

import business.service.RoomService;
import business.service.impl.RoomServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import dto.RoomDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name="CheckRoomAvailabilityServlet" , urlPatterns = "/room-avail/*")
public class CheckRoomAvailabilityServlet extends HttpServlet {
    private final RoomService roomService;
    Logger LOG =  Logger.getLogger(CheckRoomAvailabilityServlet.class.getName());

    public CheckRoomAvailabilityServlet() {
        this.roomService = new RoomServiceImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path == null) {
            path="/";
        }

        if (path.equals("/check")) {
            getAvailableRooms(request, response);
        }
    }

    private void getAvailableRooms(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try {
            LOG.log(Level.INFO, "Getting available rooms...");

            LocalDate checkIn = LocalDate.parse(request.getParameter("checkIn"));
            LocalDate checkOut = LocalDate.parse(request.getParameter("checkOut"));
            int roomTypeId = Integer.parseInt(request.getParameter("roomTypeId"));
            String[] amenities =  request.getParameterValues("amenities");
            List<Integer> amenityIds = new ArrayList<>();

            if (amenities != null) {
                for (String amenityId : amenities) {
                    amenityIds.add(Integer.parseInt(amenityId));
                }
            }
            LOG.log(Level.INFO, "Parameters are: " + checkIn + " " + checkOut + " " +  roomTypeId + " " + amenityIds.size());

            List<RoomDTO> availableRooms = roomService.findAvailableRooms(checkIn, checkOut, roomTypeId, amenityIds);
            LOG.log(Level.INFO, "Rooms are: " + availableRooms);
            // converting dates to gson to serialize
            // fallback protection to avoid errors
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd").format(src)))
                    .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src.toString()))
                    .create();

            response.getWriter().println(gson.toJson(availableRooms));

        }
        catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
