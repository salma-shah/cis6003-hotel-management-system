package business.service;

import business.service.helper.CRUDService;
import constant.ReservationStatus;
import dto.ReservationAggregrateDTO;
import dto.ReservationDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService extends CRUDService<ReservationDTO> {
    boolean makeReservation(ReservationDTO entity, List<String> selectedAmenities) ;
    boolean validateReservationDates(ReservationDTO entity) ;
    void sendSuccessfulResEmail(ReservationDTO entity) ;
    Integer findResIdByReservationNumber(String resNum) ;
    double calculateTotalCostForStay(double basePricePerNight, LocalDate startDate, LocalDate endDate, List<String> selectedAmenities) ;
    ReservationDTO getByReservationNumber(String resNum) ;
    ReservationAggregrateDTO getFullReservation(int id) ;
    boolean updateReservationStatus(int id, ReservationStatus status) ;
}
