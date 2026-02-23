package persistence.dao;

import entity.Reservation;
import persistence.dao.helper.CRUDDAO;

import java.sql.SQLException;

public interface ReservationDAO extends CRUDDAO<Reservation> {
    Reservation findByReservationNumber(String resNum) throws SQLException;
    boolean validateReservationNumber(String resNum) throws SQLException;
    Integer findReservationIdByReservationNumber(String resNum) throws SQLException;
}
