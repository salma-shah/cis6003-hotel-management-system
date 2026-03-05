package mapper;

import dto.RoomDTO;
import entity.Room;
import entity.RoomType;

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
                .roomNum(room.getRoomNum())
                .roomType(RoomTypeMapper.toRoomTypeDTO(room.getRoomType()))
                .roomStatus(room.getRoomStatus())
                .floorNum(room.getFloorNum())
                .build();
    }

    // to entity
    public static Room toRoom(RoomDTO roomDTO) {
        if (roomDTO == null) {
            return null;
        }

        RoomType roomType = new RoomType.RoomTypeBuilder().roomTypeId(roomDTO.getRoomType().getRoomTypeId()).build();
        return new Room.RoomBuilder()
                .roomId(roomDTO.getRoomId())
                .roomNum(roomDTO.getRoomNum())
                .roomType(roomType)
                .roomStatus(roomDTO.getRoomStatus())
                .floorNum(roomDTO.getFloorNum()).build();
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
