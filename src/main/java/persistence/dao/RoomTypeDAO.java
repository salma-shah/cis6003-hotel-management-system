package persistence.dao;

import entity.RoomType;
import persistence.dao.helper.CRUDDAO;

import java.sql.SQLException;

public interface RoomTypeDAO extends CRUDDAO<RoomType> {
    RoomType findByRoomId(int roomId) throws SQLException;
}
