package servlet;

import business.service.GuestService;
import business.service.impl.GuestServiceImpl;
import dto.GuestDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
//            case "/delete":   // this is for deleting a guest ; since html forms dont have DELETE,
//                deleteGuest(request, response);   // we access it using POST method
//                request.getRequestDispatcher("/user-accounts.jsp").forward(request, response);
//                break;
//            case "/update":
//                updateGuest(request, response);
//                break;
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
//            case "/all":
//                getAllGuests(request, response);
//                break;
//            case "/get":
//                getGuestDetails(request, response);
//                break;
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

            // covnerting dob to dto
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dob = sdf.parse(dateOfBirth);

            // validation


            // creating
            GuestDTO guestDTO = new GuestDTO.GuestDTOBuilder().firstName(firstName).lastName(lastName)
                    .address(address).contactNumber(contactNumber).email(email).nic(nic).passportNumber(passportNumber)
                    .registrationNumber(registrationNumber).dob(dob).build();

            boolean successfulReg = guestService.add(guestDTO);
            if (successfulReg) {
                LOG.log(Level.INFO, "Successfully registered guest" + guestDTO);
            }
            else
            {
                LOG.log(Level.SEVERE, "Failed to register guest" );
            }

        }
        catch (Exception ex) {
            LOG.warning(ex.getMessage());
        }
    }
}
