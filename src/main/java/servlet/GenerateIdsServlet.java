package servlet;

import security.GuestRegNumGenerator;
import security.IdGenerator;
import security.ReservationIdGenerator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name="GenerateIdsServlet", urlPatterns = "/generate/*")
public class GenerateIdsServlet extends HttpServlet {
    private IdGenerator idGenerator;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String typeOfId = request.getParameter("type");
        if (typeOfId.equals("reg-num"))
        {
            idGenerator = new GuestRegNumGenerator();
        }
        else if (typeOfId.equals("res-id"))
        {
            idGenerator = new ReservationIdGenerator();
        }
        else
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String id = idGenerator.generateId();
        response.setContentType("text/plain");
        response.getWriter().write(id);
    }

}
