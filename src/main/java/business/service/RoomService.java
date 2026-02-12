package business.service;

import business.service.helper.CRUDService;
import dto.RoomDTO;

import java.sql.SQLException;

public interface RoomService extends CRUDService<RoomDTO> {
    // public IRoom getRoomWithPricing(List<IRoom>);
    int addAndReturnId(RoomDTO roomDTO) throws SQLException;
}
