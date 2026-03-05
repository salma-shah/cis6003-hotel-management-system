package business.service;

import business.service.helper.CRUDService;
import dto.GuestDTO;
import dto.GuestHistoryDTO;

public interface GuestService extends CRUDService<GuestDTO> {
    boolean add(GuestDTO guestDTO);
    void validateRegistrationNumber(String registrationNumber);
    Integer findGuestIdByRegistrationNumber(String registrationNumber);
    String findGuestRegNumById(int id);
    GuestHistoryDTO getGuestHistoryById(int id);
    boolean findGuestByEmail(String email);
    boolean findGuestByNic(String nic);
    boolean findGuestByPassport(String passport);
}
