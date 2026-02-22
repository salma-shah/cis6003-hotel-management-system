package business.service.impl;

import business.service.BillService;
import business.service.GuestService;
import business.service.ReservationService;
import com.itextpdf.kernel.colors.Color;
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
    public int generateBill(String resCode, String guestCode, double stayCost, double tax, double discount,  double totalAmount) throws SQLException {

        int resId = reservationService.findResIdByReservationNumber(resCode);
        int guestId = guestService.findGuestIdByRegistrationNumber(guestCode);

        // now we build the dto
        BillDTO billDTO = new BillDTO(0, resId, guestId, tax, discount, totalAmount, stayCost);
        // checking if bill with that id exists already

        // generating bill if it exists
        Bill bill = BillMapper.toBill(billDTO);
        int billId = billDAO.generateBill(bill);
        return billId;
    }

    @Override
    public byte[] generateBillPDF(String resCode, String guestCode, double stayCost, double tax, double discount,  double totalAmount) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        document.add(new Paragraph("Ocean View Resort").setBold().setFontSize(24));

        document.add(new Paragraph("Reservation: " + resCode).setFontSize(20));
        document.add(new Paragraph("Guest ID: " + guestCode).setFontSize(20));
        document.add(new Paragraph("Stay Cost: " + totalAmount).setFontSize(20));
        document.add(new Paragraph("Tax: " + tax).setFontSize(20));
        document.add(new Paragraph("Discount: " + discount).setFontSize(20));
        document.add(new Paragraph("Total Amount: " + totalAmount).setFontSize(20));

        document.close();
        return outputStream.toByteArray();
    }

    @Override
    public int getLastInsertedId() throws SQLException {
        return billDAO.getLastInsertedId();
    }
}
