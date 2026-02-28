package persistence.dao;

import constant.PaymentStatus;
import constant.ReservationStatus;
import dto.ReservationAggregateDTO;
import entity.Reservation;
import persistence.dao.helper.CRUDDAO;

import java.time.LocalDate;
import java.util.Map;

public interface ReservationDAO extends CRUDDAO<Reservation> {
    Reservation findByReservationNumber(String resNum) ;
    boolean validateReservationNumber(String resNum) ;
    Integer findReservationIdByReservationNumber(String resNum) ;
    ReservationAggregateDTO findFullReservation(int id) ;
    PaymentStatus findPaymentStatusForReservation(int id) ;
    boolean updateReservationStatus(int id, ReservationStatus status);
    Map<String, Integer> getReservationCountByStatus(LocalDate currentDate) ;

}
