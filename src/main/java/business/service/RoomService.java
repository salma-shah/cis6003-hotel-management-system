package business.service;

import business.service.helper.CRUDService;
import dto.RoomDTO;

import java.time.LocalDate;
import java.util.List;

public interface RoomService extends CRUDService<RoomDTO> {
    // public IRoom getRoomWithPricing(List<IRoom>);
    boolean add(RoomDTO roomDTO) ;
    boolean isRoomAvailable(LocalDate checkInDate, LocalDate checkOutDate, int roomId) ;
    boolean isRoomEligible(RoomDTO roomDTO, int adults, int children) ;
    List<RoomDTO> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int roomTypeId, List<Integer> amenityIds) ;
}
