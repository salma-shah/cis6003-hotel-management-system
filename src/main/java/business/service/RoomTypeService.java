package business.service;

import dto.RoomTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface RoomTypeService {
    RoomTypeDTO getById(int id) ;
    RoomTypeDTO getByRoomId(int id) ;
    List<RoomTypeDTO> getAll() ;
}
