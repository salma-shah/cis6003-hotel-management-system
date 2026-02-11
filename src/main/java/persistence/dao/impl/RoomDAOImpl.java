package persistence.dao.impl;

import constant.BeddingTypes;
import constant.RoomStatus;
import constant.RoomTypes;
import entity.Room;
import persistence.dao.RoomDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomDAOImpl implements RoomDAO {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(RoomDAOImpl.class.getName());

    @Override
    public boolean add(Connection conn, Room entity) throws SQLException {
        try {
            return QueryHelper.execute(conn, "INSERT INTO room (description, room_type, price_per_night, bedding_type, status, max_occupancy, floor_num) VALUES (?, ?, ?, ?, ?,?,?)",
                    entity.getBaseDescription(), entity.getRoomType().toString(), entity.getBasePricePerNight(), entity.getBedding().toString(), entity.getRoomStatus().toString(), entity.getMaxOccupancy(), entity.getFloorNum());
        }
        catch (Exception ex) {
            LOG.log(Level.SEVERE, "There was an error saving the room: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean update(Connection conn, Room entity) throws SQLException {
        try
        {
            return QueryHelper.execute(conn,
                    "UPDATE room SET description=?, price_per_night=?, bedding_type=?, status=?, floor_num, WHERE room_id=?",
                    entity.getBaseDescription(), entity.getBasePricePerNight(), entity.getBedding().toString(), entity.getRoomStatus().toString(), entity.getFloorNum(), entity.getRoomId());
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error updating the room: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(Connection conn, int id) throws SQLException {
        try
        {
            return QueryHelper.execute(conn,
                    "DELETE FROM room WHERE room_id=?", id);
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error deleting the room: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public Room searchById(Connection conn, int id) throws SQLException {
        try
        {
            ResultSet resultSet =  QueryHelper.execute(conn, "SELECT * FROM room WHERE room_id=?", id);
            if (! resultSet.next())
            {
                return null;
            }
            return mapResultSetToRoom(resultSet);
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error searching the room: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public List<Room> getAll(Connection conn, Map<String, String> searchParams) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM room WHERE 1=1 ");
        // this is for the search parameters
        List<Object> params = new ArrayList<>();

        // we declare the allowed parameter keys
        Set<String> allowedParams = new HashSet<>();
        allowedParams.add("status");
        allowedParams.add("room_type");
        allowedParams.add("max_occupancy");
        allowedParams.add("bedding_type");
        allowedParams.add("floor_num");

        if (searchParams != null && !searchParams.isEmpty()) {
            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                // only using the allowed params
                if (allowedParams.contains(key)) {
                    sql.append(" AND ").append(key).append("=?");
                    params.add(value);
                }
            }
        }

        try {
            ResultSet resultSet = QueryHelper.execute(conn, sql.toString(), params.toArray());
            while (resultSet.next()) {
                rooms.add(mapResultSetToRoom(resultSet));
            }
            return rooms;  // returning the list of rooms that fit into necessary search parameters
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error searching the room: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean existsByPrimaryKey(Connection conn, int primaryKey) throws SQLException {
        try
        {
            ResultSet resultSet =  QueryHelper.execute(conn, "SELECT 1 FROM room WHERE room_id=?", primaryKey);
            return resultSet.next();
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error finding the room for the PK: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    // this is the mapping to room, based on table's column names
    private Room mapResultSetToRoom(ResultSet resultSet) throws SQLException {
        return new Room.RoomBuilder()
                .roomId(resultSet.getInt("room_id"))
                .basePricePerNight(resultSet.getDouble("price_per_night"))
                .baseDescription(resultSet.getString("description"))
                .bedding(BeddingTypes.valueOf(resultSet.getString("bedding_type")))
                .roomType(RoomTypes.valueOf(resultSet.getString("room_type")))
                .roomStatus(RoomStatus.valueOf(resultSet.getString("status")))
                .maxOccupancy(resultSet.getInt("max_occupancy"))
                .floorNum(resultSet.getInt("floor_num"))
                .build();
    }
}
