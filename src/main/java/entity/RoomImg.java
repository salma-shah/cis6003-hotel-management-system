package entity;

public class RoomImg implements SuperEntity {
    private final int  roomId;
    private final String imgPath, alt;

    public RoomImg(int  roomId, String imgPath, String alt) {
      //  this.imageId = imageId;
        this.roomId = roomId;
        this.imgPath = imgPath;
        this.alt = alt;
    }

   // public int getImageId() {
  //      return imageId;
    //}
    public int getRoomId() {
        return roomId;
    }
    public String getImgPath() {
        return imgPath;
    }
    public String getAlt() {return alt;}
}
