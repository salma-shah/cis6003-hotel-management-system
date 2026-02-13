package persistence.dao.impl;

import constant.BeddingTypes;
import constant.RoomStatus;
import constant.RoomTypes;
import entity.Room;
import entity.RoomImg;
import persistence.dao.RoomDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomDAOImpl implements RoomDAO {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(RoomDAOImpl.class.getName());

    @Override
    public boolean add(Connection conn, Room entity) throws SQLException {
        try {
            return QueryHelper.execute(conn, "INSERT INTO room (description, type, price_per_night, bedding, status, max_occupancy, floor_num) VALUES (?, ?, ?, ?, ?,?,?)",
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
                    "UPDATE room SET description=?, price_per_night=?, bedding=?, status=?, floor_num, WHERE room_id=?",
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
            ResultSet resultSet = QueryHelper.execute(conn, "SELECT * FROM room WHERE room_id =?", id);
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
        StringBuilder sql = new StringBuilder("SELECT r.room_id, r.bedding, r.type, r.status, r.description, r.price_per_night, r.floor_num, r.max_occupancy," +
                " ri.image_id, ri.room_id, ri.alt, ri.image_path" +
                " FROM room r" +
                " LEFT JOIN room_image ri ON r.room_id=ri.room_id" +
                " WHERE 1=1 ");

        // this section is for search part
        // this is for the search parameters
        List<Object> params = new ArrayList<>();

        // we declare the allowed parameter keys
        Set<String> allowedParams = new HashSet<>();
        allowedParams.add("status");
        allowedParams.add("type");
        allowedParams.add("max_occupancy");
        allowedParams.add("bedding");
        allowedParams.add("floor_num");

        if (searchParams != null && !searchParams.isEmpty()) {
            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                // since floor num and max occuapancy is numeric
                // we'll parse the int value
                if (key.equals("floor_num") || key.equals("max_occupancy")) {
                    sql.append(" AND ").append(key).append(" = ?");
                    params.add(Integer.parseInt(value));
                }

                // only using the allowed params
                if (allowedParams.contains(key)) {
                    sql.append(" AND ").append(key).append(" = ? ");
                    params.add(value);
                }
            }
        }

        // this section is for retrieving rooms with their images
        try {
            ResultSet resultSet = QueryHelper.execute(conn, sql.toString(), params.toArray());
            Map<Integer, Room> roomMap = new HashMap<>();
            while (resultSet.next()) {
                int roomId = resultSet.getInt("room_id");

                // checking if room already exists
                Room room = roomMap.get(roomId);

                if (room == null) {
                    room = mapResultSetToRoom(resultSet);
                    roomMap.put(roomId, room);
                }

            // int imageId =  resultSet.getInt("image_id");
            if (!resultSet.wasNull())
            {
                RoomImg roomImg = mapResultSetToRoomImg(resultSet);
                room.getRoomImgList().add(roomImg);
            }
            }
            return new ArrayList<>(roomMap.values());  // returning the list of rooms that fit into necessary search parameters with the images
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

    public int getLastInsertedId(Connection conn) throws SQLException {
        String sql = "SELECT LAST_INSERT_ID()"; // MySQL syntax
        try (Statement stmt = conn.createStatement();
             ResultSet resultSet = QueryHelper.execute(conn, sql)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Unable to retrieve last inserted ID");
            }
        }
    }

    // this is the mapping to room, based on table's column names
    private Room mapResultSetToRoom(ResultSet resultSet) throws SQLException {
        return new Room.RoomBuilder()
                .roomId(resultSet.getInt("room_id"))
                .basePricePerNight(resultSet.getDouble("price_per_night"))
                .baseDescription(resultSet.getString("description"))
                .bedding(BeddingTypes.valueOf(resultSet.getString("bedding")))
                .roomType(RoomTypes.valueOf(resultSet.getString("type")))
                .roomStatus(RoomStatus.valueOf(resultSet.getString("status")))
                .maxOccupancy(resultSet.getInt("max_occupancy"))
                .floorNum(resultSet.getInt("floor_num"))
                .roomImgList(new ArrayList<>())
                .build();
    }

    private RoomImg mapResultSetToRoomImg(ResultSet resultSet) throws SQLException {
        return new RoomImg(resultSet.getInt("image_id"),
                resultSet.getInt("room_id"),
                resultSet.getString("image_path"),
                resultSet.getString("alt"));
    }
}
