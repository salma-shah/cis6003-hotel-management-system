package business.service;

import business.service.helper.CRUDService;
import constant.PaymentStatus;
import constant.ReservationStatus;
import dto.ReservationAggregateDTO;
import dto.ReservationDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReservationService extends CRUDService<ReservationDTO> {
    boolean makeReservation(ReservationDTO entity, List<String> selectedAmenities) ;
    boolean validateReservationDates(ReservationDTO entity) ;
    void sendSuccessfulResEmail(ReservationDTO entity) ;
    Integer findResIdByReservationNumber(String resNum) ;
    double calculateTotalCostForStay(double basePricePerNight, LocalDate startDate, LocalDate endDate, List<String> selectedAmenities) ;
//    ReservationDTO getByReservationNumber(String resNum) ;
    ReservationAggregateDTO getFullReservation(int id) ;
    boolean updateReservationStatus(int id, ReservationStatus status) ;
    Map<String, Integer> getReservationCountByStatus(LocalDate currentDate);
    PaymentStatus getPaymentStatusByReservationId(int id);
    void validateResNum(String resNum);
}
