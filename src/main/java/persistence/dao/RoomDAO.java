package persistence.dao;

import entity.Room;
import persistence.dao.helper.CRUDDAO;

import java.sql.Connection;
import java.sql.SQLException;

public interface RoomDAO extends CRUDDAO<Room> {
    int getLastInsertedId(Connection conn) throws SQLException;
// all methods are extended from CRUD
}
