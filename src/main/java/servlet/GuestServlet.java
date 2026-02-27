package servlet;

import business.service.GuestService;
import business.service.impl.GuestServiceImpl;
import com.google.gson.*;
import dto.GuestDTO;
import dto.GuestHistoryDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
            case "/check-email":
                validateGuestIdentificationFields(request, response);
                break;
            case "/history":
                getGuestHistory(request, response);
                break;
            default:
                LOG.log(Level.SEVERE, "Unsupported path: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // registering a guest
    private void registerGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            LocalDate dob = null;

            if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                dob = LocalDate.parse(dateOfBirth);
            }

            // validation
           if (firstName == null || lastName == null || email == null || contactNumber == null || registrationNumber == null) {
               response.sendRedirect(request.getContextPath() + "/guest/register?error=empty_fields");
               return;
           }

           if (passportNumber == null && nic == null){
               response.sendRedirect(request.getContextPath() + "/guest/register?error=empty_guest_identification");
               return;
           }

           String passportRegex = "^[A-Za-z0-9]{6,9}$";
           String nicRegex= "^\\d{9,12}[vV]?$";

           if (nic != null && !nic.isEmpty()) {
               if (!nic.matches(nicRegex)) {
                   response.sendRedirect(request.getContextPath() + "/guest/register?error=invalid_nic");
                   return;
               }
           }

           if (passportNumber != null && !passportNumber.isEmpty()) {
               if (!passportNumber.matches(passportRegex)) {
                   response.sendRedirect(request.getContextPath() + "/guest/register?error=invalid_passport");
                   return;
               }
           }

            // creating
            GuestDTO guestDTO = new GuestDTO.GuestDTOBuilder().firstName(firstName).lastName(lastName)
                    .address(address).contactNumber(contactNumber).email(email).nic(nic).passportNumber(passportNumber)
                    .registrationNumber(registrationNumber).dob(dob).nationality(nationality).build();

            boolean successfulReg = guestService.add(guestDTO);
            if (successfulReg) {
                LOG.log(Level.INFO, "Successfully registered guest" + guestDTO);
                response.sendRedirect(request.getContextPath() + "/guest/register?success=true");
            } else {
                LOG.log(Level.SEVERE, "Failed to register guest");
                response.sendRedirect(request.getContextPath() + "/guest/register?error=system_error");
            }

    }

    // retrieving and displaying all guests
    private void getAllGuests(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
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
                    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
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

    // getting a single guest's details
    private void getGuestDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.log(Level.INFO, "Getting guest details method reached...");

        String id = request.getParameter("id");
        LOG.log(Level.INFO, "Guest ID passed is: " + id);

        // turning the string into an integer
        int guestId = Integer.parseInt(id);
        LOG.log(Level.INFO, "Getting guest details for guest ID :" + guestId);

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
                    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src.toString()))
                    .create();

            // using Gson to serialize
            String guestJSON = gson.toJson(guestDTO);
            response.getWriter().write(guestJSON);
            LOG.log(Level.INFO, "Guest JSON sent: " + guestJSON);

    }

    // updating a guest's details
    private void updateGuest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LOG.log(Level.INFO, "Updating guest details method reached...");
        String id = (request.getParameter("guestId"));
        int guestId = Integer.parseInt(id);

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String contactNumber = request.getParameter("contactNumber");
        String passportNumber = request.getParameter("passportNumber");
        String email = request.getParameter("email");
        String nic = request.getParameter("nic");

        // validation
        if (firstName == null || lastName == null || email == null || contactNumber == null ||
        firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || contactNumber.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/guest/all?error=empty_fields");
            return;
        }

        if (passportNumber == null && nic == null){
            response.sendRedirect(request.getContextPath() + "/guest/all?error=empty_guest_identification");
            return;
        }

        String passportRegex = "^[A-Za-z0-9]{6,9}$";
        String nicRegex= "^\\d{9,12}[vV]?$";

        if (nic != null && !nic.isEmpty()) {
            if (!nic.matches(nicRegex)) {
                response.sendRedirect(request.getContextPath() + "/guest/all?error=invalid_nic");
                return;
            }
        }

        if (passportNumber != null && !passportNumber.isEmpty()) {
            if (!passportNumber.matches(passportRegex)) {
                response.sendRedirect(request.getContextPath() + "/guest/all?error=invalid_passport");
                return;
            }
        }

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
                response.sendRedirect(request.getContextPath() + "/guest/all?success=true");
            }
            else
            {
                LOG.log(Level.INFO, "Details of guest ID: " + guestId + " was not updated");
                response.sendRedirect(request.getContextPath() + "/guest/all?error=system_error");
            }

    }

    // deleting guest
    private void deleteGuest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.log(Level.INFO, "Deleting guest method reached...");

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
                response.sendRedirect(request.getContextPath() + "/guest/all?success=success_delete");
            }
            else
            {
                LOG.log(Level.INFO, "Guest ID:" + guestId + " was not deleted.");
                response.sendRedirect(request.getContextPath() + "/guest/all?error=system_error");
            }
    }

    // guest history
    private void getGuestHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.log(Level.INFO, "Getting guest history method reached...");
        String id = request.getParameter("id");
        LOG.log(Level.INFO, "Guest ID passed is: " + id);
        int guestId = Integer.parseInt(id);
        LOG.log(Level.INFO, "Getting guest history for guest ID :" + guestId);

        GuestHistoryDTO guestHistory = guestService.getGuestHistoryById(guestId);
        if (guestHistory == null) {
            request.setAttribute("rooms", "No rooms found");
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
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.toString()))
                .create();

        // using Gson to serialize
        String guestHistoryJSON = gson.toJson(guestHistory);
        response.getWriter().write(guestHistoryJSON);
        LOG.log(Level.INFO, "Guest history JSON sent: " + guestHistoryJSON);

    }

    // validing guest input
    private void validateGuestIdentificationFields(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.log(Level.INFO, "Validating guest email...");

        String email = request.getParameter("email");
        String passport = request.getParameter("passportNumber");
        String nic = request.getParameter("nic");
        boolean nicExists = false;
        boolean passportExists = false;
        boolean emailExists = false;

        if (email != null && !email.isEmpty())
        {
            emailExists = guestService.findGuestByEmail(email);
        }

        if (passport != null && !passport.isEmpty())
        {
            passportExists = guestService.findGuestByPassport(passport);
        }

        if (nic != null && !nic.isEmpty())
        {
            nicExists = guestService.findGuestByNic(nic);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = "{"
                + "\"emailExists\": " + emailExists + ", "
                + "\"nicExists\": " + nicExists + ", "
                + "\"passportExists\": " + passportExists
                + "}";

        response.getWriter().write(json);

    }
}
