package persistence.dao;

import entity.Amenity;
import entity.Room;
import persistence.dao.helper.CRUDDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface RoomDAO extends CRUDDAO<Room> {
     int getLastInsertedId() throws SQLException;
     List<Amenity> getAmenitiesByRoomID(int roomID) throws SQLException;
     boolean isRoomAvailable(LocalDate checkInDate, LocalDate checkOutDate, int roomId) throws SQLException;
     List<Room> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int roomTypeId, List<Integer> amenityIds) throws SQLException;
// all methods are extended from CRUD
}
