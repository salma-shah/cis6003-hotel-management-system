package mapper;

import dto.RoomTypeDTO;
import entity.RoomType;

import java.util.List;
import java.util.stream.Collectors;

public class RoomTypeMapper {

    public static RoomTypeDTO toRoomTypeDTO(RoomType roomType) {
        if (roomType == null) {
            return null;
        }

        return new RoomTypeDTO.Builder()
                .roomTypeId(roomType.getRoomTypeId())
                .roomTypeName(roomType.getRoomTypeName())
                .baseDescription(roomType.getBaseDescription())
                .basePricePerNight(roomType.getBasePricePerNight())
                .bedding(roomType.getBedding())
                .totalRooms(roomType.getTotalRooms())
                .maxOccupancy(roomType.getMaxOccupancy())
                .createdAt(roomType.getCreatedAt())
                .updatedAt(roomType.getUpdatedAt())
                .amenityList(roomType.getAmenityList().stream().map(AmenityMapper::toAmenityDTO).toList())
                .roomImgList(roomType.getRoomImgList().stream().map(RoomImgMapper::toRoomImgDTO).toList())
                .build();
    }

    public static RoomType toRoomType(RoomTypeDTO roomTypeDTO) {
        if (roomTypeDTO == null) {
            return null;
        }

        return new RoomType.RoomTypeBuilder()
                .roomTypeId(roomTypeDTO.getRoomTypeId())
                .roomTypeName(roomTypeDTO.getRoomTypeName())
                .baseDescription(roomTypeDTO.getBaseDescription())
                .basePricePerNight(roomTypeDTO.getBasePricePerNight())
                .bedding(roomTypeDTO.getBedding())
                .totalRooms(roomTypeDTO.getTotalRooms())
                .maxOccupancy(roomTypeDTO.getMaxOccupancy())
                .createdAt(roomTypeDTO.getCreatedAt())
                .updatedAt(roomTypeDTO.getUpdatedAt())
                .amenityList(roomTypeDTO.getAmenityList().stream().map(AmenityMapper::toAmenity).toList())
                .roomImgList(roomTypeDTO.getRoomImgList().stream().map(RoomImgMapper::toRoomImg).toList())
                .build();
    }

    // converting list of Users to list of UserDTos
    public static List<RoomTypeDTO> roomTypeDTOList(List<RoomType> roomTypes) {
        if (roomTypes == null) {
            return null;
        }
        return roomTypes.stream()
                .map(RoomTypeMapper::toRoomTypeDTO)
                .collect(Collectors.toList());
    }
}
