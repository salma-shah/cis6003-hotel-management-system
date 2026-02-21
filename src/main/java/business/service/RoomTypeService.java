package business.service;

import dto.RoomTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface RoomTypeService {
    RoomTypeDTO getById(int id) throws SQLException;
    RoomTypeDTO getByRoomId(int id) throws SQLException;
    List<RoomTypeDTO> getAll() throws SQLException;
}
