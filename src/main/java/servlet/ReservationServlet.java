package servlet;

import business.service.GuestService;
import business.service.ReservationService;
import business.service.impl.GuestServiceImpl;
import business.service.impl.ReservationServiceImpl;
import constant.ReservationStatus;
import dto.ReservationDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ReservationServlet", urlPatterns = "/reservation/*")
public class ReservationServlet extends HttpServlet {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(ReservationServlet.class.getName());
    private ReservationService reservationService;
    private GuestService guestService;

    @Override
    public void init() {
        this.reservationService = new ReservationServiceImpl();
        this.guestService = new GuestServiceImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path =  request.getPathInfo();
        if (path == null) {
            path="/";
        }

        switch (path){
            case "/":
                request.getRequestDispatcher("/reservations.jsp").forward(request, response);
                break;
            case "/create":
                request.getRequestDispatcher("/create-reservation.jsp").forward(request, response);
                break;
            case "/cost":
                getTotalCost(request, response);
                break;
                default:
                    LOG.log(Level.SEVERE, "Unsupported path: " + path);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path =  request.getPathInfo();
        if (path == null) {
            path="/";
        }

       switch (path){
           case "/create":
               makeReservation(request, response);
               break;
      }
    }

    // making a reservation
    private void makeReservation(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        try {
            LOG.log(Level.INFO, "Making reservation method reached...");

            // getting the required parameters
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            LOG.log(Level.INFO, "Room Id: " + roomId);

            // getting guest id by reservation number
            Integer guestIdObj = guestService.findGuestIdByRegistrationNumber(request.getParameter("guestRegNumber"));
            if (guestIdObj == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Guest not found for registration number: " + guestIdObj);
                return;
            }
            int guestId = guestIdObj;


            String reservationNum = request.getParameter("reservationNumber");
            int numAdults = Integer.parseInt(request.getParameter("numAdults"));
            int numChildren = Integer.parseInt(request.getParameter("numChildren"));
            double totalPrice = Double.parseDouble(request.getParameter("totalCost"));
            LocalDate checkInDate = LocalDate.parse(request.getParameter("checkInDate"));
            LocalDate checkOutDate = LocalDate.parse(request.getParameter("checkOutDate"));
            LocalDateTime dateOfRes =  LocalDateTime.now();

            // getting the amenities with prices
            // this gets the values of the amenities
            String[] selectedAmenities = request.getParameterValues("amenities");
            List<String> selectedAmenitiesList = Arrays.asList(selectedAmenities);
            if (selectedAmenitiesList != null){
                Collections.emptyList();
            }

            // do validation


            // if everything is passed, reservation will be made
            ReservationDTO reservationDTO = new ReservationDTO(0, guestId, roomId, reservationNum, totalPrice, dateOfRes, checkInDate, checkOutDate, numAdults, numChildren, ReservationStatus.Pending);
            boolean successfulRes = reservationService.makeReservation(reservationDTO, selectedAmenitiesList);
            if (successfulRes) {
                LOG.log(Level.INFO, "Reservation made successfully...");
            }
            else   {
                LOG.log(Level.SEVERE, "Reservation made failed...");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // calculating total cost
    private void getTotalCost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


}

