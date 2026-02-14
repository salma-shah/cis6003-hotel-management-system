package persistence.dao;

import entity.SuperEntity;

import java.sql.Connection;
import java.sql.SQLException;

public interface RoomImgDAO {
    boolean saveImg(int roomId, String imgPath, String alt) throws SQLException;
}
