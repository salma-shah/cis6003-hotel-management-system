package persistence.dao;

import java.time.LocalDate;
import java.util.Map;

public interface ReportDAO {
    int getTotalReservations(LocalDate startDate, LocalDate endDate);
    Map<String, Integer> getReservationsByStatus(LocalDate startDate, LocalDate endDate);
    double getTotalStayRevenue(LocalDate startDate, LocalDate endDate);
    double getTotalNetRevenue(LocalDate startDate, LocalDate endDate);
    double getTotalTax(LocalDate startDate, LocalDate endDate);
    int getNewGuests(LocalDate startDate, LocalDate endDate);
    int getReturnedGuests(LocalDate startDate, LocalDate endDate);
    Map<String, Integer> getPaymentStatusBreakdown(LocalDate startDate, LocalDate endDate);
    double getPaidAmounts(LocalDate startDate, LocalDate endDate);
    double getOutstandingRevenue(LocalDate startDate, LocalDate endDate);
}
