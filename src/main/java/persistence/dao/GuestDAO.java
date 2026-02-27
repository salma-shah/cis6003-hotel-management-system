package persistence.dao;

import dto.GuestHistoryDTO;
import entity.Guest;
import persistence.dao.helper.CRUDDAO;

public interface GuestDAO extends CRUDDAO<Guest> {
    boolean existsByRegistrationNumber(String registrationNumber);
    Integer findGuestIdByRegistrationNumber(String registrationNumber) ;
    boolean findByField(String fieldName, String fieldValue) ;
    String findGuestRegNumById(int id) ;
    GuestHistoryDTO getGuestHistoryById(int id) ;
}
