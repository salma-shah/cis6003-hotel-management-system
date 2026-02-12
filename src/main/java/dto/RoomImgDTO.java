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

    private RoomImgDTO (RoomImgDTO.RoomImgDTOBuilder builder)
    {
        this.imageId = builder.imageId;
        this.roomId = builder.roomId;
        this.imgPath = builder.imgPath;
        this.alt = builder.alt;
    }

    public static class RoomImgDTOBuilder
    {
        private int imageId;
        private int roomId;
        private String imgPath;
        private String alt;

        public RoomImgDTO.RoomImgDTOBuilder imageId(int imageId)
        {
            this.imageId = imageId;
            return this;
        }

        public RoomImgDTO.RoomImgDTOBuilder roomId(int roomId)
        {
            this.roomId = roomId;
            return this;
        }

        public RoomImgDTO.RoomImgDTOBuilder imgPath(String imgPath)
        {
            this.imgPath = imgPath;
            return this;
        }

        public RoomImgDTO.RoomImgDTOBuilder alt(String alt)
        {
            this.alt = alt;
            return this;
        }

        public RoomImgDTO build()
        {
            return new RoomImgDTO(this);
        }
    }

}
