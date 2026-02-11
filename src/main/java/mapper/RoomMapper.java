package mapper;

import dto.RoomDTO;
import entity.Room;

import java.util.List;
import java.util.stream.Collectors;

public class RoomMapper {
    // to dto
    public static RoomDTO toRoomDTO(Room room) {

        if (room == null) {
            return null;
        }

        return new RoomDTO.RoomDTOBuilder()
                .roomId(room.getRoomId())
                .baseDescription(room.getBaseDescription())
                .basePricePerNight(room.getBasePricePerNight())
                .roomType(room.getRoomType())
                .roomStatus(room.getRoomStatus())
                .bedding(room.getBedding())
                .maxOccupancy(room.getMaxOccupancy())
                .floorNum(room.getFloorNum())
                .build();
    }

    // to entity
    public static Room toRoom(RoomDTO roomDTO) {
        if (roomDTO == null) {
            return null;
        }

        return new Room.RoomBuilder()
                .roomId(roomDTO.getRoomId())
                .baseDescription(roomDTO.getDescription())
                .basePricePerNight(roomDTO.getPricePerNight())
                .roomType(roomDTO.getRoomType())
                .roomStatus(roomDTO.getRoomStatus())
                .bedding(roomDTO.getBeddingTypes())
                .maxOccupancy(roomDTO.getMaxOccupancy())
                .floorNum(roomDTO.getFloorNum())
                .build();
    }

    // converting to a DTO List
    public static List<RoomDTO> toRoomDTOList(List<Room> rooms) {
        if (rooms == null) {
            return null;
        }
        return rooms.stream()
                .map(RoomMapper:: toRoomDTO)
                .collect(Collectors.toList());

    }
}
