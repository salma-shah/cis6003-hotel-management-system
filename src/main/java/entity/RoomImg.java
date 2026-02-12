package entity;

public class RoomImg implements SuperEntity {
    private final int  imageId, roomId;
    private final String imgPath, alt;

    public RoomImg(int imageId, int roomId, String imgPath, String alt) {
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
    public String getAlt() {return alt;}

    @Override
    public String toString() {
        return "RoomImg{" +
                "imageId=" + imageId +
                ", roomId=" + roomId +
                ", imgPath='" + imgPath + '\'' +
                ", alt='" + alt + '\'' +
                '}';
    }

    private RoomImg (RoomImgBuilder builder)
    {
        this.imageId = builder.imageId;
        this.roomId = builder.roomId;
        this.imgPath = builder.imgPath;
        this.alt = builder.alt;
    }

    public static class RoomImgBuilder
    {
        private int imageId;
        private int roomId;
        private String imgPath;
        private String alt;

        public RoomImgBuilder imageId(int imageId)
        {
            this.imageId = imageId;
            return this;
        }

        public RoomImgBuilder roomId(int roomId)
        {
            this.roomId = roomId;
            return this;
        }

        public  RoomImgBuilder imgPath(String imgPath)
        {
            this.imgPath = imgPath;
            return this;
        }

        public RoomImgBuilder alt(String alt)
        {
            this.alt = alt;
            return this;
        }

        public RoomImg build()
        {
            return new RoomImg(this);
        }
    }
}
