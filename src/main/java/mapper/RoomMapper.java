package mapper;

import dto.RoomDTO;
import dto.RoomImgDTO;
import entity.Room;
import entity.RoomImg;

import java.util.List;
import java.util.stream.Collectors;

public class RoomMapper {
    // to dto
    public static RoomDTO toRoomDTO(Room room) {

        if (room == null) {
            return null;
        }

        List<RoomImgDTO> imageDTOs = room.getRoomImgList().stream().map(RoomImgMapper::toRoomImgDTO).collect(Collectors.toList());

        return new RoomDTO.RoomDTOBuilder()
                .roomId(room.getRoomId())
                .baseDescription(room.getBaseDescription())
                .basePricePerNight(room.getBasePricePerNight())
                .roomType(room.getRoomType())
                .roomStatus(room.getRoomStatus())
                .bedding(room.getBedding())
                .maxOccupancy(room.getMaxOccupancy())
                .floorNum(room.getFloorNum())
                .roomImgList(imageDTOs)
                .build();
    }

    // to entity
    public static Room toRoom(RoomDTO roomDTO) {
        if (roomDTO == null) {
            return null;
        }

        List<RoomImg> roomImg = roomDTO.getRoomImgList().stream().map(RoomImgMapper::toRoomImg).collect(Collectors.toList());

        return new Room.RoomBuilder()
                .roomId(roomDTO.getRoomId())
                .baseDescription(roomDTO.getDescription())
                .basePricePerNight(roomDTO.getPricePerNight())
                .roomType(roomDTO.getRoomType())
                .roomStatus(roomDTO.getRoomStatus())
                .bedding(roomDTO.getBeddingTypes())
                .maxOccupancy(roomDTO.getMaxOccupancy())
                .floorNum(roomDTO.getFloorNum())
              .roomImgList(roomImg)
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
