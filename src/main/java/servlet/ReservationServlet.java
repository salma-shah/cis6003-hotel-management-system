package servlet;

import business.service.GuestService;
import business.service.ReservationService;
import business.service.RoomTypeService;
import business.service.impl.GuestServiceImpl;
import business.service.impl.ReservationServiceImpl;
import business.service.impl.RoomTypeServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import constant.PaymentStatus;
import constant.ReservationStatus;
import dto.ReservationAggregateDTO;
import dto.ReservationDTO;
import dto.RoomTypeDTO;
import exception.guest.GuestNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ReservationServlet", urlPatterns = "/reservation/*")
public class ReservationServlet extends HttpServlet {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(ReservationServlet.class.getName());
    private ReservationService reservationService;
    private GuestService guestService;
    private RoomTypeService roomTypeService;

    @Override
    public void init() {
        this.reservationService = new ReservationServiceImpl();
        this.guestService = new GuestServiceImpl();
        this.roomTypeService = new RoomTypeServiceImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        switch (path) {
            case "/create":
                request.getRequestDispatcher("/create-reservation.jsp").forward(request, response);
                break;
            case "/all":
                    getAllReservations(request, response);
                    break;
            case "/details":
                    getFullDetailsForReservations(request,response);
                    break;
            default:
                LOG.log(Level.SEVERE, "Unsupported path: " + path);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        switch (path) {
            case "/create":
                makeReservation(request, response);
                break;
            case "/calculate":
                    calculateAmenities(request, response);
                    break;
            case "/status":
                    updateStatus(request, response);
                break;
        }
    }

    // making a reservation
    private void makeReservation(HttpServletRequest request, HttpServletResponse response) throws IOException {
            LOG.log(Level.INFO, "Making reservation method reached...");

            // getting the required parameters
        String reservationNum = request.getParameter("reservationNumber");
        String numAdultsStr = request.getParameter("numAdults");
        String numChildrenExists = request.getParameter("numChildren");
        int numChildren = 0;
        if (numChildrenExists != null) {
            numChildren = Integer.parseInt(numChildrenExists);
        }

        if (numAdultsStr == null || numAdultsStr.isEmpty())
        {
            response.sendRedirect(request.getContextPath() + "/reservation/create?error=adult_guest_required");
            return;
        }
        int numAdults = Integer.parseInt(request.getParameter("numAdults"));

        String checkInDate = request.getParameter("checkInDate");
        String checkOutDate = request.getParameter("checkOutDate");
        LocalDateTime dateOfRes = LocalDateTime.now();

        // ensuring a room is selected
        int roomId = 0;
        roomId = Integer.parseInt(request.getParameter("roomId"));
        if (roomId <= 0)
        {
            response.sendRedirect(request.getContextPath() + "/reservation/create?error=invalid_room");
            return;
        }
        String totalPriceStr = request.getParameter("totalCost");
        String[] selectedAmenities = request.getParameterValues("amenities");

        // validations
        if (reservationNum == null || reservationNum.isEmpty() || numAdults <= 0 || totalPriceStr == null
        || checkInDate == null || checkOutDate == null || selectedAmenities == null || selectedAmenities.length == 0) {
            response.sendRedirect(request.getContextPath() + "/reservation/create?error=empty_fields");
            return;
        }

        double totalPrice = Double.parseDouble(request.getParameter("stayCost"));
        if (totalPrice <= 0)
        {
            response.sendRedirect(request.getContextPath() + "/reservation/create?error=empty_cost");
            return;
        }
        LocalDate checkIn = LocalDate.parse(request.getParameter("checkInDate"));
        LocalDate checkOut = LocalDate.parse(request.getParameter("checkOutDate"));

        if (checkIn.isAfter(checkOut)) {
            response.sendRedirect(request.getContextPath() + "/reservation/create?error=checkindate_after_checkout");
            return;
        }

        if (checkIn.isBefore(ChronoLocalDate.from(dateOfRes)) || checkOut.isBefore(ChronoLocalDate.from(dateOfRes))) {
            response.sendRedirect(request.getContextPath() + "/reservation/create?error=dates_in_past");
            return;
        }
            // getting guest id by reservation number
            Integer guestIdObj;
        try {
            guestIdObj = guestService.findGuestIdByRegistrationNumber(
                    request.getParameter("guestRegNumber")
            );
        } catch (GuestNotFoundException | IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/reservation/create?error=guest_not_found");
            return;
        }
            int guestId = guestIdObj;
            LOG.log(Level.INFO, "Guest id: " + guestId);

            // getting the amenities with prices
            // this gets the values of the amenities
            LOG.log(Level.INFO, "Amenities: " + Arrays.toString(selectedAmenities));
            List<String> selectedAmenitiesList =
                selectedAmenities != null
                        ? Arrays.asList(selectedAmenities)
                        : new ArrayList<>();


            // if everything is passed, reservation will be made
            ReservationDTO reservationDTO = new ReservationDTO(0, guestId, roomId, reservationNum, totalPrice, dateOfRes, checkIn, checkOut, numAdults, numChildren, ReservationStatus.Pending);
            boolean successfulRes = reservationService.makeReservation(reservationDTO, selectedAmenitiesList);
            if (successfulRes) {
                LOG.log(Level.INFO, "Reservation made successfully...");

                // sending total cost
                double amenitiesCost = Double.parseDouble(request.getParameter("amenitiesCost"));
                double totalCost = amenitiesCost + totalPrice;
                response.sendRedirect(request.getContextPath() + "/reservation/create?success=true&reservationNum=" + reservationNum + "&totalCost=" + totalCost + "&guestId=" + guestId);  }
            else
            {
                LOG.log(Level.SEVERE, "Reservation made failed...");
                response.sendRedirect(request.getContextPath() + "/reservation/create?error=system_error");
            }
    }

    // calculating only amenities cost to display dynamically
    private void calculateAmenities(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] amenities = request.getParameterValues("amenities");
        List<String> list = amenities != null
                ? Arrays.asList(amenities)
                : Collections.emptyList();

        int roomId = Integer.parseInt(request.getParameter("roomId"));
        LOG.log(Level.INFO, "Room Id: " + roomId);
        RoomTypeDTO roomTypeDTO = roomTypeService.getByRoomId(roomId);
        double basePrice = roomTypeDTO.getBasePricePerNight();
        LOG.log(Level.INFO, "Base Price: " + basePrice);
        LocalDate checkIn = LocalDate.parse(request.getParameter("checkInDate"));
        LocalDate checkOut = LocalDate.parse(request.getParameter("checkOutDate"));

        long numOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        double total = reservationService.calculateTotalCostForStay(
                basePrice, checkIn, checkOut, list
        );

        // displaying only the amenities cost
        double amenitiesCost = total - (basePrice * numOfNights);
        LOG.log(Level.INFO, "Amenities Cost: " + amenitiesCost);
        response.setContentType("application/json");
        response.getWriter().write("{\"amenitiesCost\":"  + amenitiesCost + "}");
    }

    // getting all reservations
    private void getAllReservations(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        LOG.log(Level.INFO, "Getting all reservations...");
        Map<String, String> searchParams = new HashMap<>();

            String resNum = request.getParameter("resSearchInput");
            String status = request.getParameter("statusInput");
            String checkIn = request.getParameter("checkInDate");
            String checkOut = request.getParameter("checkOutDate");

            if (resNum != null && !resNum.isEmpty()) {
                searchParams.put("reservation_number", resNum);
                LOG.log(Level.INFO, "Searching for res num : " + resNum);
            }

            if (status != null && !status.isEmpty()) {
                searchParams.put("status", status);
                LOG.log(Level.INFO, "Searching for Status : " + status);
            }

            // date filtering
            if(checkIn!= null && !checkIn.isEmpty()) {
                searchParams.put("checkin_date", checkIn);
                LOG.log(Level.INFO, "Searching for check_in: " + checkIn);
            }

            if(checkOut != null && !checkOut.isEmpty()) {
                searchParams.put("checkout_date", checkOut);
                LOG.log(Level.INFO, "Searching for check_out: " + checkOut);
            }

            LOG.log(Level.INFO, "Searching for reservations by : " + searchParams);
            List<ReservationDTO> reservations = reservationService.getAll(searchParams);

            // converting dates into serializable
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
                List<ReservationDTO> reservationDTOList = (reservations != null) ? reservations : Collections.emptyList();
                String resJSON = gson.toJson(reservationDTOList);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(resJSON);
                LOG.log(Level.INFO, "Reservation JSON sent: " + resJSON);
                return;
            }

            // if no search req, then just the rooms will be populated
            request.setAttribute("reservations", reservations);
            LOG.log(Level.INFO, "Reservations found: " + reservations.size());
            request.getRequestDispatcher("/reservations.jsp").forward(request, response);
    }

    // getting full details for a reservation
    private void getFullDetailsForReservations(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.log(Level.INFO, "Getting full details for reservations...");
        int id = Integer.parseInt(request.getParameter("id"));
        LOG.log(Level.INFO, "ID: " + id);

        // getting the full details using res aggregate
        ReservationAggregateDTO reservationAggregateDTO = reservationService.getFullReservation(id);

        // converting dates into serializable
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd").format(src)))
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.toString()))
                .create();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = gson.toJson(reservationAggregateDTO);
        response.getWriter().write(json);
        LOG.log(Level.INFO, "Full details found: " + json);
    }

    // updating a reservation's status
    private void updateStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.log(Level.INFO, "Updating status of reservations...");

        int id = Integer.parseInt(request.getParameter("id"));
        LOG.log(Level.INFO, "ID: " + id);

        ReservationStatus status = ReservationStatus.valueOf(request.getParameter("status"));
        LOG.log(Level.INFO, "Status: " + status);


        // checking whether a reservation is paid before checkout
        if (status == ReservationStatus.CheckedOut)
        {
            PaymentStatus paymentStatus = reservationService.getPaymentStatusByReservationId(id);
            if (paymentStatus != PaymentStatus.Paid)
            {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String json = "{\"error\": true}";
                response.getWriter().write(json);
                return;
            }
        }

        // updating the status
        boolean updatedStatus = reservationService.updateReservationStatus(id, status);

        LOG.log(Level.INFO, "Updated status of reservation: " + id);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = "{\"success\": " + updatedStatus + "}";
        response.getWriter().write(json);

    }

}

