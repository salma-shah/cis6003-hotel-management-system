package servlet;

import business.service.GuestService;
import business.service.impl.GuestServiceImpl;
import com.google.gson.*;
import dto.GuestDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name="GuestServlet", urlPatterns = "/guest/*")
public class GuestServlet extends HttpServlet {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(GuestServlet.class.getName());
    private GuestService guestService;

    @Override
    public void init() {
        this.guestService = new GuestServiceImpl() {
        };
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
            case "/register":   // this is for registering guests
                registerGuest(request, response);
                break;
            case "/delete":   // this is for deleting a guest
                deleteGuest(request, response);   // we access it using POST method
                break;
            case "/update":
                updateGuest(request, response);
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
            case "/":
                // showing registration form
                request.getRequestDispatcher("/register-guest.jsp").forward(request, response);
                break;
            case "/all":
                getAllGuests(request, response);
                break;
            case "/register":
                request.getRequestDispatcher("/register-guest.jsp").forward(request, response);
                break;
            case "/get":
                getGuestDetails(request, response);
                break;
            default:
                LOG.log(Level.SEVERE, "Unsupported path: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // registering a guest
    private void registerGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            LOG.log(Level.INFO, "Registering guest");

            // getting parameters
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String contactNumber = request.getParameter("contactNumber");
            String nic = request.getParameter("nic");
            String passportNumber = request.getParameter("passportNumber");
            String registrationNumber = request.getParameter("registrationNumber");
            String dateOfBirth = request.getParameter("dob");
            String nationality = request.getParameter("nationality");

            // converting dob to dto
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dob = sdf.parse(dateOfBirth);

            // validation


            // creating
            GuestDTO guestDTO = new GuestDTO.GuestDTOBuilder().firstName(firstName).lastName(lastName)
                    .address(address).contactNumber(contactNumber).email(email).nic(nic).passportNumber(passportNumber)
                    .registrationNumber(registrationNumber).dob(dob).nationality(nationality).build();

            boolean successfulReg = guestService.add(guestDTO);
            if (successfulReg) {
                LOG.log(Level.INFO, "Successfully registered guest" + guestDTO);
            } else {
                LOG.log(Level.SEVERE, "Failed to register guest");
            }

        } catch (Exception ex) {
            LOG.warning(ex.getMessage());
        }
    }

    // retrieving and displaying all guests
    private void getAllGuests(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            LOG.log(Level.INFO, "Getting all guests");
            Map<String, String> searchParams = new HashMap<>();

            // checking if params are passed
            String nicOrPassportNumber = request.getParameter("nicOrPPSearchText");
            String registrationNumber = request.getParameter("regSearchText");
            String status = request.getParameter("statusText");

            if (nicOrPassportNumber != null && !nicOrPassportNumber.isEmpty()) {

                if(nicOrPassportNumber.length() > 9)
                {
                    String nicParam = nicOrPassportNumber;
                    searchParams.put("nic", nicParam);
                    LOG.log(Level.INFO, "Searching for NIC : " + nicParam);
                }
                else
                {
                    String passportParam = nicOrPassportNumber;
                    searchParams.put("passport_number", passportParam);
                    LOG.log(Level.INFO, "Searching for Passport num : " + passportParam);
                }
            }

            if ( registrationNumber != null && !registrationNumber.isEmpty()) {
                searchParams.put("registration_number", registrationNumber);
                LOG.log(Level.INFO, "Searching for Registration num : " + registrationNumber);
            }

            if ( status != null && !status.isEmpty()) {
                searchParams.put("status", status);
                LOG.log(Level.INFO, "Searching for Status : " + status);
            }

            List<GuestDTO> guests = guestService.getAll(searchParams);
//
//            // converting dob into being serializable
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd").format(src)))
                    .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src.toString()))
                    .create();

            // this is if js sends a search request
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                List<GuestDTO> guestList = (guests != null) ? guests : Collections.emptyList();
                String guestJSON = gson.toJson(guestList);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(guestJSON);
                LOG.log(Level.INFO, "Guest JSON sent: " + guestJSON);
                return;
            }

            // if no search req, then just the rooms will be populated
            request.setAttribute("guests", guests);
            LOG.log(Level.INFO, "Guests found: " + guests.size());
            request.getRequestDispatcher("/guests.jsp").forward(request, response);
        }
        catch (Exception ex) {
            LOG.warning(ex.getMessage());
        }
    }

    // getting a single guest's details
    private void getGuestDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.log(Level.INFO, "Getting guest details method reached...");

        String id = request.getParameter("id");
        LOG.log(Level.INFO, "Guest ID passed is: " + id);

        // turning the string into an integer
        int guestId = Integer.parseInt(id);
        LOG.log(Level.INFO, "Getting guest details for guest ID :" + guestId);

        try {
            if (guestId == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The guestID is missing.");
                return;
            }

            // if guest id exists, details for that id will be passed as JSON to frontend
            GuestDTO guestDTO = guestService.searchById(guestId);

            if (guestDTO == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Guest not found");
                return;
            }

            // converting user to JSON for JavaScript to recognize
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // converting dob into being serializable
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd").format(src)))
                    .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src.toString()))
                    .create();

            // using Gson to serialize
            String guestJSON = gson.toJson(guestDTO);
            response.getWriter().write(guestJSON);
            LOG.log(Level.INFO, "Guest JSON sent: " + guestJSON);

        }
        catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error fetching guest details: " + ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }

    // updating a guest's details
    private void updateGuest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LOG.log(Level.INFO, "Updating guest details method reached...");
        String id = (request.getParameter("guestId"));
        int guestId = Integer.parseInt(id);

        try
        {
            GuestDTO guest = new GuestDTO.GuestDTOBuilder().id(guestId)
                    .firstName(request.getParameter("firstName"))
                    .lastName(request.getParameter("lastName"))
                    .address(request.getParameter("address"))
                    .contactNumber(request.getParameter("contactNumber"))
                    .passportNumber(request.getParameter("passportNumber"))
                    .build();

            System.out.println("Updating guest with ID: " + guest.getId());

            // if guest exists, fields will be updated
            boolean isUpdated = guestService.update(guest);
            if (isUpdated) {
                LOG.log(Level.INFO, "Guest with ID: " + guestId + "was updated successfully");
                response.sendRedirect(request.getContextPath() + "/guest/all");
            }
            else
            {
                LOG.log(Level.INFO, "Details of guest ID: " + guestId + " was not updated");
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error updating the guest details: " + ex);
        }

    }

    // deleting guest
    private void deleteGuest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.log(Level.INFO, "Deleting guest method reached...");
        try
        {
            String id = request.getParameter("guestId");
            int guestId = Integer.parseInt(id);
            if (guestId == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            boolean isDeleted = guestService.delete(guestId);
            if (isDeleted)
            {
                LOG.log(Level.INFO, "Guest ID:" + guestId + " was deleted successfully");
                response.sendRedirect(request.getContextPath() + "/guest/all");
            }
            else
            {
                LOG.log(Level.INFO, "Guest ID:" + guestId + " was not deleted.");
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error deleting guest:" +  ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
