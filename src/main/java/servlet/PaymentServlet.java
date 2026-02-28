package servlet;

import business.service.BillService;
import business.service.PaymentService;
import business.service.impl.BillServiceImpl;
import business.service.impl.PaymentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "PaymentServlet", urlPatterns = "/payment/*")
public class PaymentServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(PaymentServlet.class.getName());
    private PaymentService paymentService;
    private BillService billService;

    @Override
    public void init()
    {
        this.paymentService = new PaymentServiceImpl();
        this.billService = new BillServiceImpl();
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
        else if(path.equals("/form")) {req.getRequestDispatcher("/make-payment.jsp").forward(req, resp);}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            path = "/";
        }

        if (path.equals("/generate")) {
                generateBill(req, resp);
        }
    }

    private void generateBill(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        LOG.log(Level.INFO, "Generate bill method reached...");

            // getting the params
            String resNum = req.getParameter("reservationNum");
            int guestId = Integer.parseInt(req.getParameter("guestId"));
            String stayCostStr = req.getParameter("stayCost");
            String discountStr = req.getParameter("discount");
            String totalStr = req.getParameter("amount");
            String taxStr = req.getParameter("tax");

            // discount is optional
            double discount = 0.0;
            if (discountStr != null && !discountStr.isEmpty()) discount = Double.parseDouble(discountStr);

           // validations
            if (stayCostStr == null || totalStr == null || taxStr == null || taxStr.isEmpty() || stayCostStr.isEmpty() || totalStr.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/payment/generate?error=empty_fields");
                return;
            }

            double stayCost = Double.parseDouble(stayCostStr);
            double tax = Double.parseDouble(taxStr);
            double totalAmount = Double.parseDouble(totalStr);

            int billId = billService.generateBill(resNum, guestId, stayCost,tax, discount);
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
                    resp.sendRedirect(req.getContextPath() + "/payment?error=payment_system_error");
                }

                // generating the pdf for the bill
                byte[] billPDF = billService.generateBillPDF(billId, resNum, guestId, stayCost, tax, discount, totalAmount);
                resp.setContentType("application/pdf");
                resp.setHeader("Content-Disposition", "attachment; filename=OceanView_Bill_ " + billId + "_forRes" + resNum + ".pdf");
                resp.getOutputStream().write(billPDF);
            } else {
                LOG.log(Level.INFO, "Something went wrong");
                resp.sendRedirect(req.getContextPath() + "/payment?error=bill_system_error");
            }
    }

}
