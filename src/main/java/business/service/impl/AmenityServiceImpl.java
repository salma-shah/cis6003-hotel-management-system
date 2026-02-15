package business.service.impl;

import business.service.AmenityService;
import db.DBConnection;
import persistence.dao.AmenityDAO;
import persistence.dao.UserDAO;
import persistence.dao.impl.AmenityDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AmenityServiceImpl implements AmenityService {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(AmenityServiceImpl.class.getName());
    private final AmenityDAO amenityDAO;

    public AmenityServiceImpl() {
        this.amenityDAO = new AmenityDAOImpl();
    }

    @Override
    public boolean addAmenityToRoom(int roomId, int amenityId) throws SQLException {
    try (Connection con = DBConnection.getInstance().getConnection()) {
        LOG.log(Level.INFO, "Entering addAmenityToRoom");
        return amenityDAO.addAmenityToRoom(roomId, amenityId);
    } catch (SQLException ex) {
        LOG.log(Level.SEVERE, null, ex);
        throw new SQLException(ex.getMessage());
    }
}

}
