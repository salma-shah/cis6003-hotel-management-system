package persistence.dao;

import entity.Guest;
import persistence.dao.helper.CRUDDAO;

import java.sql.SQLException;

public interface GuestDAO extends CRUDDAO<Guest> {
    boolean findByRegistrationNumber(String registrationNumber) throws SQLException;
    Integer findGuestIdByRegistrationNumber(String registrationNumber) throws SQLException;
    boolean findByEmail(String email) throws SQLException;
}
