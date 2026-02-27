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

        if (path.equals("/")) {
            req.getRequestDispatcher("/help.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            path = "/";
        }

        if (path.equals("/contact-form")) {
            createContactForm(req, resp);
        } else {
            LOG.log(Level.SEVERE, "Unsupported path: " + path);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createContactForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String message = req.getParameter("message");

            if (message == null) {
                req.setAttribute("emptyMsgError", "You need to enter your query.");
                req.getRequestDispatcher("/help.jsp").forward(req, resp);
                return;
            }

            // ensuring user is logged in
            HttpSession session = req.getSession();
            if (session == null || session.getAttribute("userId") == null) {

            req.setAttribute("userInvalidError", "You need to log in to submit the form.");
            req.getRequestDispatcher("/help.jsp").forward(req, resp);
            return;
        }
                int userId = (int) session.getAttribute("userId");
                if (userId > 0) {
                    LOG.log(Level.INFO, "User ID of logged in user is: " + userId);

                    // since user is logged in, their contact form will be saved
                    ContactFormDTO contactFormDTO = new ContactFormDTO(0, userId, message);
                    boolean successfulForm = contactFormService.saveForm(contactFormDTO);
                    if (successfulForm) {
                        LOG.log(Level.INFO, "Successfully saved the form");
                        req.setAttribute("successfulFormMsg", "Your query was submitted successfully! We will get back to you soon!");
                        req.getRequestDispatcher("/help.jsp").forward(req, resp);
                        return;
                    }
                    else
                    {
                        req.setAttribute("formError", "Something went wrong with submitting the form.");
                        req.getRequestDispatcher("/help.jsp").forward(req, resp);
                        return;
                    }
                } else {
                    LOG.log(Level.INFO, "User ID of logged in user is null.");
                }
    }
}
