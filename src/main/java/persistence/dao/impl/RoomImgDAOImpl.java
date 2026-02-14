package persistence.dao.impl;

import db.DBConnection;
import entity.RoomImg;
import entity.SuperEntity;
import persistence.dao.RoomImgDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class RoomImgDAOImpl implements RoomImgDAO {
    // enabling logging in tomcat server
   // private static final Logger LOG = Logger.getLogger(RoomDAOImpl.class.getName());

    @Override
    public boolean saveImg(int roomId, String imgPath, String alt) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection();)
        {
            return QueryHelper.execute(conn, "INSERT INTO room_image (room_id, image_path, alt) VALUES(?,?, ?)", roomId,
                    imgPath, alt);
        }
        catch (Exception e)
        {
            throw new  SQLException(e.getMessage());
        }
    }
}
