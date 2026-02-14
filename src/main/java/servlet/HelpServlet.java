package servlet;

import business.service.ContactFormService;
import business.service.impl.ContactFormServiceImpl;
import dto.ContactFormDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name= "HelpServlet", urlPatterns = "/help/*")
public class HelpServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(HelpServlet.class.getName());
    private ContactFormService contactFormService;

    @Override
    public void init() {
        this.contactFormService = new ContactFormServiceImpl() {
        };
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            path = "/";
        }

        switch (path) {
            case "/":
                req.getRequestDispatcher("/help.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            path = "/";
        }

        switch (path) {
            case "/contact-form":
                createContactForm(req, resp);
                break;

            default:
            LOG.log(Level.SEVERE, "Unsupported path: " + path);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createContactForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String message = req.getParameter("message");

            // ensuring user is logged in
            HttpSession session = req.getSession();
            if (session != null) {
                int userId = (int) session.getAttribute("userId");
                if (userId > 0) {
                    LOG.log(Level.INFO, "User ID of logged in user is: " + userId);

                    // since user is logged in, their contact form will be saved
                    ContactFormDTO contactFormDTO = new ContactFormDTO(0, userId, message);
                    boolean successfulForm = contactFormService.saveForm(contactFormDTO);
                    if (successfulForm) {
                        LOG.log(Level.INFO, "Successfully saved the form");
                    }
                    else
                    {
                        LOG.log(Level.INFO, "Failed to save the form");
                    }
                } else {
                    LOG.log(Level.INFO, "User ID of logged in user is null.");
                }
            } else {
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
