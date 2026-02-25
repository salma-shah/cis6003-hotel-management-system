package persistence.dao.impl;

import db.DBConnection;
import exception.db.DataAccessException;
import persistence.dao.RoomImgDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.SQLException;

public class RoomImgDAOImpl implements RoomImgDAO {
    // enabling logging in tomcat server
   // private static final Logger LOG = Logger.getLogger(RoomDAOImpl.class.getName());

    @Override
    public boolean saveImg(int roomId, String imgPath, String alt) {
        try(Connection conn = DBConnection.getInstance().getConnection();)
        {
            return QueryHelper.executeUpdate(conn, "INSERT INTO room_image (room_id, image_path, alt) VALUES(?,?, ?)", roomId,
                    imgPath, alt);
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }
}
