package dto;


public class RoomImgDTO implements SuperDTO {
        private final int imageId, roomId;
        private final String imgPath, alt;

        public RoomImgDTO( int imageId, int roomId, String imgPath, String alt ) {
            this.imageId = imageId;
            this.roomId = roomId;
            this.imgPath = imgPath;
            this.alt = alt;
        }

        public int getImageId() {
            return imageId;
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
