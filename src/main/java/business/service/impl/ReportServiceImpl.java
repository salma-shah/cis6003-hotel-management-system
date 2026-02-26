package business.service.impl;

import business.service.ReportService;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import dto.ReportDTO;
import persistence.dao.ReportDAO;
import persistence.dao.impl.ReportDAOImpl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Map;

public class ReportServiceImpl implements ReportService {
    private ReportDAO reportDAO;

    public ReportServiceImpl() {
        this.reportDAO = new ReportDAOImpl();
    }
    @Override
    public ReportDTO generateReport(LocalDate startDate, LocalDate endDate) {
        int totalReservations =reportDAO.getTotalReservations(startDate, endDate);
        double totalStayRevenue = reportDAO.getTotalStayRevenue(startDate, endDate);
        double totalTax = reportDAO.getTotalTax(startDate, endDate);
        double totalNetRevenue = reportDAO.getTotalNetRevenue(startDate, endDate);
        double paidAmount = reportDAO.getPaidAmounts(startDate, endDate);
        double outstandingAmount = reportDAO.getOutstandingRevenue(startDate, endDate);
        int newGuests = reportDAO.getNewGuests(startDate, endDate);
        int returnedGuests = reportDAO.getReturnedGuests(startDate, endDate);
        Map<String, Integer> paymentStatusBreakdown = reportDAO.getPaymentStatusBreakdown(startDate, endDate);
        Map<String, Integer> reservationStatusBreakdown = reportDAO.getReservationsByStatus(startDate, endDate);

        // reservations statuses
        int confirmedRes = reservationStatusBreakdown.getOrDefault("Confirmed", 0);
        int cancelledRes = reservationStatusBreakdown.getOrDefault("Cancelled", 0);
        int checkedInRes = reservationStatusBreakdown.getOrDefault("CheckedIn", 0);

        // paid statuses
        int paidBills = paymentStatusBreakdown.getOrDefault("Paid", 0);
        int outstandingBills = paymentStatusBreakdown.getOrDefault("Pending", 0);

        // building the report dto
        return new ReportDTO(
                totalReservations, confirmedRes, checkedInRes, cancelledRes,totalNetRevenue,totalStayRevenue, totalTax,
                newGuests, returnedGuests, paidAmount, outstandingAmount, paidBills, outstandingBills);

    }

    @Override
    public byte[] generateReportPDF(LocalDate startDate, LocalDate endDate, ReportDTO reportDTO) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        Color titleColour = new DeviceRgb(36, 90, 119);
        document.add(new Paragraph("Welcome to Ocean View Resort").setBold().setFontSize(20).setFontColor(titleColour));
        document.add(new Paragraph("View the report between " + startDate + " and " + endDate + " below.").setFontSize(14));
        document.add(new Paragraph(""));

        // creating the table
        float[] pointColumnWidth = {250F, 250F};
        Table table = new Table(pointColumnWidth);
        table.setWidth(500);

        // res
        table.addCell(new Cell().add(new Paragraph("Total Reservations: " + reportDTO.getTotalReservations())));
        table.addCell(new Cell().add(new Paragraph("Checked-In Reservations: " + reportDTO.getCheckedInReservations())));
        table.addCell(new Cell().add(new Paragraph("Confirmed Reservations: " + reportDTO.getConfirmedReservations())));
        table.addCell(new Cell().add(new Paragraph("Cancelled Reservations: " + reportDTO.getCancelledReservations())));

        // revenue
        table.addCell(new Cell().add(new Paragraph("Stay Revenue: " + reportDTO.getTotalStayRevenue()))).setMinHeight(55f);
        table.addCell(new Cell().add(new Paragraph("Tax Collected: " + reportDTO.getTotalTax()))).setMinHeight(55f);

        // guests
        table.addCell(new Cell().add(new Paragraph("New Guests: " + reportDTO.getNewGuests())));
        table.addCell(new Cell().add(new Paragraph("Returning Guests: " + reportDTO.getReturnedGuests())));

        // payments
        table.addCell(new Cell().add(new Paragraph("Paid Amounts: " + reportDTO.getPaidAmounts())));
        table.addCell(new Cell().add(new Paragraph("Outstanding Amounts: " + reportDTO.getOutstandingAmounts())));
        table.addCell(new Cell().add(new Paragraph("Paid Bills: " + reportDTO.getPaidBills())));
        table.addCell(new Cell().add(new Paragraph("Outstanding Bills: " + reportDTO.getOutstandingBills())));

        table.addCell(new Cell().add(new Paragraph("Total Revenue: " + reportDTO.getTotalNetRevenue())));

        document.add(new Paragraph("Please carefully review the report and sign.").setFontSize(13));
        document.add(new Paragraph(""));


        document.add(table);
        document.close();
        return outputStream.toByteArray();
    }
}
