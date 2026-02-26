package servlet;

import business.service.ReportService;
import business.service.impl.GuestServiceImpl;
import business.service.impl.ReportServiceImpl;
import dto.ReportDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

@WebServlet(name= "ReportServlet", urlPatterns = "/report/*")
public class ReportServlet extends HttpServlet {
    private ReportService reportService;

    @Override
    public void init() {
        this.reportService = new ReportServiceImpl() {
        };
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }
        if (path.equals("/generate")) {
            generateReport(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        if (path.equals("/")) {
            request.getRequestDispatcher("/report.jsp").forward(request, response);
        }
    }

    private void generateReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        LocalDate start;
        LocalDate end;

        String type = request.getParameter("reportType");
//        String startDateString = request.getParameter("startDate");
//        String endDateString = request.getParameter("endDate");
//        LocalDate startDate = LocalDate.parse(startDateString);
//        LocalDate endDate = LocalDate.parse(endDateString);
//
        // selecting the report type
        if (type.equals("weekly")) {
            start = LocalDate.now().minusDays(7);
            end = LocalDate.now();
        }
        else
        {
            start = YearMonth.now().atDay(1);
            end = LocalDate.now();
        }

        // generating the report
        ReportDTO report = reportService.generateReport(start, end);
        byte[] reportPDF = reportService.generateReportPDF(start, end, report);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=OceanViewResortReport_" + start + "_TO_" + end+ "_.pdf");
        response.getOutputStream().write(reportPDF);
    }
}
