package dto;

import entity.SuperEntity;

public class RoomImgDTO implements SuperDTO {

        private final int roomId;
        private final String imgPath, alt;

        public RoomImgDTO( int roomId, String imgPath, String alt ) {
            this.roomId = roomId;
            this.imgPath = imgPath;
            this.alt = alt;
        }
        public int getRoomId() {
            return roomId;
        }
        public String getImgPath() {
            return imgPath;
    }
    public String getAlt() {
            return alt;
    }

}
