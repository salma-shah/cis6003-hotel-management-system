package persistence.dao;

import entity.Amenity;
import entity.Room;
import persistence.dao.helper.CRUDDAO;

import java.time.LocalDate;
import java.util.List;

public interface RoomDAO extends CRUDDAO<Room> {
     int getLastInsertedId() ;
     List<Amenity> getAmenitiesByRoomID(int roomID) ;
     boolean isRoomAvailable(LocalDate checkInDate, LocalDate checkOutDate, int roomId) ;
     List<Room> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int roomTypeId, List<Integer> amenityIds) ;
// all methods are extended from CRUD
}
