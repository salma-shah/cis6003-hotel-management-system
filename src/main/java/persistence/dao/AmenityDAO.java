package persistence.dao;

import java.sql.SQLException;

public interface AmenityDAO {
    boolean addAmenityToRoom(int roomId, int amenityId) throws SQLException;
}
