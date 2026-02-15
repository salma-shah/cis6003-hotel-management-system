package persistence.dao.impl;

import db.DBConnection;
import persistence.dao.AmenityDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AmenityDAOImpl implements AmenityDAO {
    private static final Logger LOG = Logger.getLogger(RoomDAOImpl.class.getName());


    @Override
    public boolean addAmenityToRoom(int roomId, int amenityId) throws SQLException {
       try(Connection con = DBConnection.getInstance().getConnection()) {
           LOG.log(Level.INFO, "Entering addAmenityToRoom in DAO");
           return QueryHelper.execute(con, "INSERT INTO room_amenity (room_id, amenity_id) VALUES (?, ?)", new Object[]{roomId, amenityId});
       }
       catch(SQLException ex) {
           LOG.log(Level.SEVERE, ex.getMessage(), ex);
           throw new SQLException(ex.getMessage());
       }
    }
}
