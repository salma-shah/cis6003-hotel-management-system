package persistence.dao;

import entity.Reservation;
import persistence.dao.helper.CRUDDAO;

import java.sql.SQLException;

public interface ReservationDAO extends CRUDDAO<Reservation> {
    Reservation findByReservationNumber() throws SQLException;
    boolean validateReservationNumber(String resNum) throws SQLException;
}
