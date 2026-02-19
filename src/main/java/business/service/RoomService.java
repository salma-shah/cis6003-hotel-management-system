package business.service;

import business.service.helper.CRUDService;
import dto.RoomDTO;

import java.sql.SQLException;

public interface RoomService extends CRUDService<RoomDTO> {
    // public IRoom getRoomWithPricing(List<IRoom>);
    boolean add(RoomDTO roomDTO) throws SQLException;
    int addAndReturnId(RoomDTO roomDTO) throws SQLException;
    boolean isRoomEligible(RoomDTO roomDTO, int adults, int children) throws SQLException;
}
