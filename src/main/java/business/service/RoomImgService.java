package business.service;

import dto.RoomImgDTO;

import java.sql.SQLException;

public interface RoomImgService {
    boolean saveImg(RoomImgDTO roomImgDTO) throws SQLException;
}
