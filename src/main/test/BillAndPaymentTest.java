import business.service.BillService;
import business.service.PaymentService;
import business.service.impl.BillServiceImpl;
import business.service.impl.PaymentServiceImpl;
import org.junit.Test;

public class BillAndPaymentTest {

    // testing to make generate bill and make payment
    @Test
    public void test_generatingBill() {
        BillService billService = new BillServiceImpl();
        double totalAmount = 20800.00;
        double tax = 0.1 * totalAmount;
        billService.generateBill("RVZ35Y9A93N8", 6, 6, tax, 0.1);
    }

    @Test
    public void test_generatingPaymentForBill() {
        PaymentService paymentService = new PaymentServiceImpl();
        paymentService.processPayment(2, 13800, "Cash");
    }

}
