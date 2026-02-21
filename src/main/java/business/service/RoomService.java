package business.service;

import business.service.helper.CRUDService;
import dto.RoomDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface RoomService extends CRUDService<RoomDTO> {
    // public IRoom getRoomWithPricing(List<IRoom>);
    boolean add(RoomDTO roomDTO) throws SQLException;
    boolean isRoomAvailable(LocalDate checkInDate, LocalDate checkOutDate, int roomId) throws SQLException;
    boolean isRoomEligible(RoomDTO roomDTO, int adults, int children) throws SQLException;
    List<RoomDTO> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int roomTypeId, List<Integer> amenityIds) throws SQLException;
}
