package business.service;

import business.service.helper.CRUDService;
import dto.GuestDTO;

import java.sql.SQLException;

public interface GuestService extends CRUDService<GuestDTO> {
    boolean add(GuestDTO guestDTO) throws SQLException;
    boolean validateRegistrationNumber(String registrationNumber) throws SQLException;
    Integer findGuestIdByRegistrationNumber(String registrationNumber) throws SQLException;
}
