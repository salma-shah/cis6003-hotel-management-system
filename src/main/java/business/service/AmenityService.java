package business.service;

import java.sql.SQLException;

public interface AmenityService {
    boolean addAmenityToRoom(int roomId, int amenityId) throws SQLException;
}
