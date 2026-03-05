package persistence.dao;

public interface RoomImgDAO {
    boolean saveImg(int roomId, String imgPath, String alt);
}
