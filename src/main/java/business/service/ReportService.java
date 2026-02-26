package business.service;

import dto.ReportDTO;

import java.time.LocalDate;

public interface ReportService {
    ReportDTO generateReport(LocalDate startDate, LocalDate endDate);
    byte[] generateReportPDF(LocalDate startDate, LocalDate endDate, ReportDTO reportDTO);
}
