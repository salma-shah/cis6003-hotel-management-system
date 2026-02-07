package servlet;

import mail.EmailUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

@WebServlet(name = "EmailServlet", urlPatterns = "/email/*")
public class EmailServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(EmailServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //  String path = request.getPathInfo();
        processEmailRequest(request, response);

    }

    // method to send email
    private void processEmailRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String emailRecipient = request.getParameter("recipient");
//        String emailSubject = request.getParameter("subject");
//        String emailBody = request.getParameter("body");

        String sender, receiver, password, subject,
                message, title;

        title = request.getParameter("title");
        sender = request.getParameter("emailSender");
        receiver = request.getParameter("emailReceiver");
        password = request.getParameter("password");
        subject = request.getParameter("subject");
        message = request.getParameter("message");

        try
        {
            request.setAttribute("message", EmailUtility.sendMail(title, receiver, sender, subject, message, true, password));
            getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
        }
        catch (Exception ex)
        {
            LOG.warning(ex.getMessage());
        }

    }
}
