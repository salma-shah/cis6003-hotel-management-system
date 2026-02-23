package business.service.impl;

import business.service.BillService;
import business.service.GuestService;
import business.service.ReservationService;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import dto.BillDTO;
import entity.Bill;
import mapper.BillMapper;
import persistence.dao.BillDAO;
import persistence.dao.impl.BillDAOImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillServiceImpl implements BillService {
    private final BillDAO billDAO;
    private final ReservationService reservationService;
    private final GuestService guestService;
    private static final Logger LOG = Logger.getLogger(BillServiceImpl.class.getName());

    public BillServiceImpl() {
        this.billDAO = new  BillDAOImpl();
        this.reservationService = new ReservationServiceImpl();
        this.guestService = new GuestServiceImpl();
    }

    @Override
    public int generateBill(String resCode, int guestId, double stayCost, double tax, double discount) throws SQLException {

        int resId = reservationService.findResIdByReservationNumber(resCode);
        // int guestId = guestService.findGuestIdByRegistrationNumber(guestCode);

        double totalAmount = calculateTotalAmount(stayCost, tax, discount);
        // now we build the dto
        BillDTO billDTO = new BillDTO(0, resId, guestId, tax, discount, totalAmount, stayCost);

        // generating bill if it exists
        Bill bill = BillMapper.toBill(billDTO);
        return billDAO.generateBill(bill);
    }

    @Override
    public byte[] generateBillPDF(int billId, String resCode, int guestId, double stayCost, double tax, double discount,  double totalAmount) {

       try {
           ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
           PdfWriter pdfWriter = new PdfWriter(outputStream);
           PdfDocument pdfDocument = new PdfDocument(pdfWriter);
           Document document = new Document(pdfDocument);

           // getting guest code by guest id
           String guestCode = guestService.findGuestRegNumById(guestId);

           // custom font colour for test
           Color titleColour = new DeviceRgb(36, 90, 119);
           document.add(new Paragraph("Welcome to Ocean View Resort").setBold().setFontSize(24).setFontColor(titleColour));
           document.add(new Paragraph("View the details of your bill below:").setFontSize(22));
           document.add(new Paragraph(""));
           document.add(new Paragraph("Bill ID: " + billId).setBold().setFontSize(18));
           document.add(new Paragraph("Reservation: " + resCode).setFontSize(18));
           document.add(new Paragraph("Guest ID: " + guestCode).setFontSize(18));
           document.add(new Paragraph("Stay Cost: " + stayCost).setFontSize(18));
           document.add(new Paragraph("Tax: " + tax).setFontSize(18));
           document.add(new Paragraph("Discount: " + discount).setFontSize(18));
           document.add(new Paragraph("Total Amount: " + totalAmount).setFontSize(18));

           document.add(new Paragraph(""));
           document.add(new Paragraph("We hope you have a pleasant stay at Ocean View Resort. Thank you!")).setBold().setFontSize(17);

           document.close();
           return outputStream.toByteArray();
       }
       catch (SQLException ex) {
           LOG.log(Level.SEVERE, null, ex);
          // throw new SQLException(ex.getMessage());
           return null;
       }
    }

    @Override
    public double calculateTotalAmount(double stayCost, double tax, double discount) throws SQLException {
        double totalAmount = 0;
        if  (stayCost > 0 && tax > 0 && discount > 0) {
            totalAmount = (stayCost + tax) - ( stayCost * discount);
        }
        if  (stayCost > 0 && tax > 0 && discount <= 0) {
            totalAmount = stayCost + tax;
        }

        return totalAmount;
    }
}
