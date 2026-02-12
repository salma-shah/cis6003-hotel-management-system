package mapper;

import dto.RoomImgDTO;
import entity.RoomImg;

public class RoomImgMapper {
    public static RoomImgDTO toRoomImgDTO(RoomImg roomImg){
        if(roomImg == null){
            return null;
        }

        return new RoomImgDTO.RoomImgDTOBuilder()
                .roomId(roomImg.getRoomId())
                .imgPath(roomImg.getImgPath())
                .alt(roomImg.getAlt())
                .build();
    }

    public static RoomImg toRoomImg(RoomImgDTO roomImgDTO){
        if(roomImgDTO == null){
            return null;
        }

        return new RoomImg.RoomImgBuilder().roomId(roomImgDTO.getRoomId()).imgPath(roomImgDTO.getImgPath()).alt(roomImgDTO.getAlt()).build(); }
}
