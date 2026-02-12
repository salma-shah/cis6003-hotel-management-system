package mapper;

import dto.RoomImgDTO;
import entity.RoomImg;

public class RoomImgMapper {
    public static RoomImgDTO toRoomImgDTO(RoomImg roomImg){
        if(roomImg == null){
            return null;
        }

        return new RoomImgDTO(roomImg.getRoomId(), roomImg.getImgPath(), roomImg.getAlt());
    }

    public static RoomImg toRoomImg(RoomImgDTO roomImgDTO){
        if(roomImgDTO == null){
            return null;
        }

        return new RoomImg(roomImgDTO.getRoomId(), roomImgDTO.getImgPath(), roomImgDTO.getAlt());
    }
}
