package persistence.dao;

import constant.ReservationStatus;
import dto.ReservationAggregrateDTO;
import entity.Reservation;
import persistence.dao.helper.CRUDDAO;

import java.sql.SQLException;

public interface ReservationDAO extends CRUDDAO<Reservation> {
    Reservation findByReservationNumber(String resNum) ;
    boolean validateReservationNumber(String resNum) ;
    Integer findReservationIdByReservationNumber(String resNum) ;
    ReservationAggregrateDTO findFullReservation(int id) ;
    boolean updateReservationStatus(int id, ReservationStatus status) ;
}
