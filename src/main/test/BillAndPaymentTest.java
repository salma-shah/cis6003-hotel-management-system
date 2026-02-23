import business.service.BillService;
import business.service.PaymentService;
import business.service.impl.BillServiceImpl;
import business.service.impl.PaymentServiceImpl;
import constant.PaymentStatus;
import dto.BillDTO;
import dto.PaymentDTO;
import org.junit.Test;


import java.sql.SQLException;
import java.time.LocalDateTime;

public class BillAndPaymentTest {

    // testing to make generate bill and make payment
    @Test
    public void test_generatingBill() throws SQLException {
        BillService billService = new BillServiceImpl();
        double totalAmount = 20800.00;
        double tax = 0.1 * totalAmount;
        billService.generateBill("RV12345O2J", 6, 6, tax, 0.1);
    }

    @Test
    public void test_generatingPaymentForBill() throws SQLException {
        PaymentService paymentService = new PaymentServiceImpl();
        LocalDateTime timeOfPayment = LocalDateTime.now();
        paymentService.processPayment(2, 13800, "Cash");
    }

}
