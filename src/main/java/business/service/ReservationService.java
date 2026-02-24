package business.service;

import business.service.helper.CRUDService;
import constant.ReservationStatus;
import dto.ReservationAggregrateDTO;
import dto.ReservationDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService extends CRUDService<ReservationDTO> {
    boolean makeReservation(ReservationDTO entity, List<String> selectedAmenities) throws SQLException;
    boolean validateReservationDates(ReservationDTO entity) throws SQLException;
    void sendSuccessfulResEmail(ReservationDTO entity) throws SQLException;
    Integer findResIdByReservationNumber(String resNum) throws SQLException;
    double calculateTotalCostForStay(double basePricePerNight, LocalDate startDate, LocalDate endDate, List<String> selectedAmenities) throws SQLException;
    ReservationDTO getByReservationNumber(String resNum) throws SQLException;
    ReservationAggregrateDTO getFullReservation(int id) throws SQLException;
    boolean updateReservationStatus(int id, ReservationStatus status) throws SQLException;
}
