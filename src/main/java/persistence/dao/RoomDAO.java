package persistence.dao;

import entity.Amenity;
import entity.Room;
import persistence.dao.helper.CRUDDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoomDAO extends CRUDDAO<Room> {
    int getLastInsertedId(Connection conn) throws SQLException;
    List<Amenity> getAmenitiesByRoomID(Connection conn, int roomID) throws SQLException;
// all methods are extended from CRUD
}
