package servlet;

import business.service.BillService;
import business.service.PaymentService;
import business.service.ReservationService;
import business.service.impl.BillServiceImpl;
import business.service.impl.PaymentServiceImpl;
import business.service.impl.ReservationServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "PaymentServlet", urlPatterns = "/payment/*")
public class PaymentServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(PaymentServlet.class.getName());
    private PaymentService paymentService;
    private ReservationService reservationService;
    private BillService billService;

    @Override
    public void init()
    {
        this.paymentService = new PaymentServiceImpl();
        this.billService = new BillServiceImpl();
        this.reservationService = new ReservationServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            path = "/";
        }

        if (path.equals("/")) {
            req.getRequestDispatcher("/make-payment.jsp").forward(req, resp);
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            path = "/";
        }

        if (path.equals("/generate")) {
            try {
                generateBill(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void generateBill(HttpServletRequest req, HttpServletResponse resp) throws SQLException{

        LOG.log(Level.INFO, "Generate bill method reached...");

        try {
            // getting the params
            String resNum = req.getParameter("reservationNum");
            String guestId = req.getParameter("guestId");
            double stayCost = Double.parseDouble(req.getParameter("stayCost"));
            double discount = Double.parseDouble(req.getParameter("discount"));
            double tax = Double.parseDouble(req.getParameter("tax"));
            double totalAmount = Double.parseDouble(req.getParameter("amount"));

            // validations

            int billId = billService.generateBill(resNum, guestId, stayCost,tax, discount, totalAmount);
            if (billId > 0) {
                LOG.log(Level.INFO, "Bill generated successfully...");

                // making payment
                String paymentMethod =  req.getParameter("paymentMethod");

                boolean successfulPayment = paymentService.processPayment(billId, totalAmount, paymentMethod);
                if (successfulPayment) {
                    LOG.log(Level.INFO, "Payment successful...");
                }
                else
                {
                    LOG.log(Level.INFO, "Payment failed...");
                }
                // generating the pdf for the bill
                byte[] billPDF = billService.generateBillPDF(resNum, guestId, tax, discount, totalAmount, stayCost);
                resp.setContentType("application/pdf");
                resp.setHeader("Content-Disposition", "attachment; filename=OV_Bill_ " + resNum + ".pdf");
                resp.getOutputStream().write(billPDF);

            } else {
                LOG.log(Level.INFO, "Something went wrong");
            }
        }
        catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw  new SQLException(ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
