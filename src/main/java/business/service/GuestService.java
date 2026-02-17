package business.service;

import business.service.helper.CRUDService;
import dto.GuestDTO;

import java.sql.SQLException;

public interface GuestService extends CRUDService<GuestDTO> {
    boolean add(GuestDTO guestDTO) throws SQLException;
    boolean findByRegistrationNumber(String registrationNumber) throws SQLException;
}
