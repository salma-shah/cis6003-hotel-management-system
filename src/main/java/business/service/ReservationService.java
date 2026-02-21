package business.service;

import business.service.helper.CRUDService;
import dto.ReservationDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService extends CRUDService<ReservationDTO> {
    boolean makeReservation(ReservationDTO entity, List<String> selectedAmenities) throws SQLException;
    boolean validateReservationDates(ReservationDTO entity) throws SQLException;
    void sendSuccessfulResEmail(ReservationDTO entity) throws SQLException;
    double calculateTotalCostForStay(double basePricePerNight, LocalDate startDate, LocalDate endDate, List<String> selectedAmenities) throws SQLException;
}
