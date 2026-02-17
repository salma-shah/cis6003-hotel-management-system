package persistence.dao.impl;

import constant.BeddingTypes;
import constant.RoomStatus;
import constant.RoomTypes;
import db.DBConnection;
import entity.Amenity;
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
import java.util.stream.Collectors;

public class RoomDAOImpl implements RoomDAO {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(RoomDAOImpl.class.getName());

    @Override
    public boolean add(Room entity) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()){
            return QueryHelper.execute(conn, "INSERT INTO room (description, type, price_per_night, bedding, status, max_occupancy, floor_num) VALUES (?, ?, ?, ?, ?,?,?)",
                    entity.getBaseDescription(), entity.getRoomType().toString(), entity.getBasePricePerNight(), entity.getBedding().toString(), entity.getRoomStatus().toString(), entity.getMaxOccupancy(), entity.getFloorNum());
        }
        catch (Exception ex) {
            LOG.log(Level.SEVERE, "There was an error saving the room: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean update(Room entity) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
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
    public boolean delete(int id) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
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
    public Room searchById( int id) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
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
    public List<Room> getAll(Map<String, String> searchParams) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            List<Room> rooms = new ArrayList<>();
            Map<Integer, Room> roomMap = new HashMap<>();
            StringBuilder sql = new StringBuilder(
                    "SELECT r.room_id, r.bedding, r.type, r.status, r.description, r.price_per_night, " +
                            "r.floor_num, r.max_occupancy, COUNT(r.room_id) OVER(PARTITION BY r.type, r.bedding, r.max_occupancy) AS total_rooms " +
                            "FROM room r WHERE 1=1 "
            );

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
                while (resultSet.next()) {
                    int roomId = resultSet.getInt("room_id");

                    // checking if room already exists
                    Room room = roomMap.get(roomId);

                    if (room == null) {
                        roomMap.put(roomId, mapResultSetToRoom(resultSet));
                    }

                    if (roomMap.isEmpty()) {
                        return Collections.emptyList();
                    }

                    // we prepare these general placeholders only once
                    String placeholders = roomMap.keySet().stream().map(k -> "?").collect(Collectors.joining(","));
                    List<Object> idParams = new ArrayList<>(roomMap.keySet());

                    // images section
                    String imgSql = "SELECT ri.image_id, ri.room_id, ri.alt, ri.image_path " +
                            "FROM room_image ri WHERE ri.room_id IN (" + placeholders + ")";
                    ResultSet imageResultSet = QueryHelper.execute(conn, imgSql, idParams.toArray());

                    while (imageResultSet.next()) {
                        int imgRoomId = imageResultSet.getInt("room_id");
                        RoomImg roomImg = mapResultSetToRoomImg(imageResultSet);
                        room = roomMap.get(imgRoomId);
                        if (room != null) {
                            room.getRoomImgList().add(roomImg);
                        }
                    }

                    // now amenities
                    String amenitySql = "SELECT ra.room_id, a.amenity_id, a.name, a.cost " +
                            "FROM amenity a " +
                            "INNER JOIN room_amenity ra ON ra.amenity_id = a.amenity_id " +
                            "WHERE ra.room_id IN (" + placeholders + ")";

                    ResultSet amenityResultSet = QueryHelper.execute(conn, amenitySql, idParams.toArray());
                    while (amenityResultSet.next()) {
                        int roomAmenityId = amenityResultSet.getInt("room_id");
                        room = roomMap.get(roomAmenityId);
                        if (room != null) {
                            room.getAmenityList().add(mapResultSetToAmenity(amenityResultSet));
                        }
                    }
                }
                return new ArrayList<>(roomMap.values());  // returning the list of rooms that fit into necessary search parameters with the images

            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "There was an error searching the room: ", ex);
                throw new SQLException(ex.getMessage());
            }
        }
    }


    @Override
    public boolean existsByPrimaryKey( int primaryKey) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
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

    @Override
    public List<Amenity> getAmenitiesByRoomID(int roomID) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            List<Amenity> amenities = new ArrayList<>();
            ResultSet resultSet = QueryHelper.execute(conn, "SELECT a.id, a.name " +
                    "FROM amenity a " +
                    "INNER JOIN room_amenity ra ON ra.amenity_id = a.id " +
                    "WHERE ra.room_id = ? ", roomID);
            while (resultSet.next()) {
                amenities.add(mapResultSetToAmenity(resultSet));
            }
            return amenities;
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
                .totalRooms(resultSet.getInt("total_rooms"))
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

    private Amenity mapResultSetToAmenity(ResultSet resultSet) throws SQLException {
        return new Amenity(resultSet.getInt("amenity_id"),
                resultSet.getString("name"),
                resultSet.getDouble("cost"));
    }
}
