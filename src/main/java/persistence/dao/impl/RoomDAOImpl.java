package persistence.dao.impl;

import constant.BeddingTypes;
import constant.RoomStatus;
import db.DBConnection;
import entity.Amenity;
import entity.Room;
import entity.RoomImg;
import entity.RoomType;
import persistence.dao.RoomDAO;
import persistence.dao.RoomTypeDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RoomDAOImpl implements RoomDAO {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(RoomDAOImpl.class.getName());
    private final RoomTypeDAO roomTypeDAO ;

    public RoomDAOImpl() {
        this.roomTypeDAO = new  RoomTypeDAOImpl();
    }

    @Override
    public boolean add(Room entity) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()){
            return QueryHelper.execute(conn, "INSERT INTO room (room_number, room_type_id, floor_num, room_status) VALUES (?, ?, ?, ?)",
                   entity.getRoomNum(), entity.getRoomType().getRoomTypeId(), entity.getFloorNum(), entity.getRoomStatus().name());
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
                    "UPDATE room SET room_status WHERE room_id=?", entity.getRoomStatus(), entity.getRoomId());

        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error updating the room status: ", ex);
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
            if (!resultSet.next())
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
            Map<Integer, RoomType> roomTypeMap = new HashMap<>();

            // this is to retrieve all rooms based on room types
            StringBuilder roomSql = new StringBuilder(
                    "SELECT r.*, rt.* " +
                            "FROM room r JOIN room_type rt ON r.room_type_id = rt.room_type_id " +
                            "WHERE 1=1"
            );

            // this section is for search part
            // this is for the search parameters
            List<Object> params = new ArrayList<>();

            if (searchParams != null && !searchParams.isEmpty()) {
                for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    // since floor num and max occupancy is numeric
                    // we'll parse the int value
                    switch (key) {
                        case "status"-> {
                            roomSql.append(" AND r.room_status=? ");   // appending room status to the SQL string builder
                            params.add(value);  // adding status as a parameter
                        }
                        // we do the same for everything else
                        case "name"-> {
                            roomSql.append(" AND rt.name=? ");
                            params.add(value);
                        }
                        case "max_occupancy"-> {
                            roomSql.append(" AND rt.max_occupancy=? ");
                            params.add(value);
                        }
                        case "bedding"-> {
                            roomSql.append(" AND rt.bedding=? ");
                            params.add(value);
                        }
                        case "floor_num"-> {
                            roomSql.append(" AND r.floor_num=? ");
                            params.add(value);
                        }
                    }
                }

                // adding filter by checkin/checkout dates
                if (searchParams.containsKey("check_in") && searchParams.containsKey("check_out")) {
                    roomSql.append(" AND NOT EXISTS ( SELECT 1 FROM reservation rv WHERE rv.room_id = r.room_id AND rv.status='Confirmed' AND (? < rv.checkout_date AND ? > checkin_date) ) ");
                    params.add(searchParams.get("check_in"));
                    params.add(searchParams.get("check_out"));
                }
            }

            // this section is for retrieving rooms with their images
            try {
                // first we execute the room query and return a result set to it
                ResultSet resultSet = QueryHelper.execute(conn, roomSql.toString(), params.toArray());
                while (resultSet.next()) {
                    int roomTypeId = resultSet.getInt("room_type_id");
                    RoomType roomType = roomTypeMap.get(roomTypeId);

                    // mapping the value to room type
                    if (roomType == null) {
                        roomType = mapResultSetToRoomType(resultSet);
                        roomTypeMap.put(roomTypeId, roomType);
                    }

                    // mapping to room map
                    Room room = new Room.RoomBuilder().roomId(resultSet.getInt("room_id"))
                                    .roomNum(resultSet.getString("room_number")).floorNum(resultSet.getInt("floor_num"))
                                    .roomStatus(RoomStatus.valueOf(resultSet.getString("room_status"))).roomType(roomType).build();
                    roomMap.put(room.getRoomId(), room);  // by using room ID, we ensure that it is unique

                }
                    if (roomMap.isEmpty()) {
                        return Collections.emptyList();
                    }

                    // we prepare these general placeholders only once to reduce redundancy
                    String placeholders = roomTypeMap.keySet().stream().map(k -> "?").collect(Collectors.joining(","));
                    List<Object> idParams = new ArrayList<>(roomTypeMap.keySet());

                    // images section
                    String imgSql = "SELECT * FROM room_img WHERE room_type_id IN (" + placeholders + ")";
                    ResultSet imageResultSet = QueryHelper.execute(conn, imgSql, idParams.toArray());

                    while (imageResultSet.next()) {
                        int imgRoomTypeId = imageResultSet.getInt("room_type_id");  // getting the id based on where image matches with room type id
                        RoomImg roomImg = mapResultSetToRoomImg(imageResultSet);
                        RoomType roomType = roomTypeMap.get(imgRoomTypeId);
                        if (roomType != null) {
                            roomType.getRoomImgList().add(roomImg);  // attaching the img to room type which we are using throughout the query
                        }
                    }

                    // now amenities
                    String amenitySql = "SELECT rta.room_type_id, a.* FROM amenity a JOIN room_type_amenity rta ON rta.amenity_id = a.amenity_id WHERE rta.room_type_id IN (" + placeholders + ")";

                    ResultSet amenityResultSet = QueryHelper.execute(conn, amenitySql, idParams.toArray());
                    while (amenityResultSet.next()) {  // following the same approach here
                        int roomTypeAmenityId = amenityResultSet.getInt("room_type_id");
                        RoomType roomType = roomTypeMap.get(roomTypeAmenityId);
                        if (roomType != null) {
                            roomType.getAmenityList().add(mapResultSetToAmenity(amenityResultSet));
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

    @Override
    public int getLastInsertedId() throws SQLException {
        String sql = "SELECT LAST_INSERT_ID()"; // mysql syntax
        try (Connection connection = DBConnection.getInstance().getConnection())
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
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
                    "WHERE ra.room_type_id = ? ", roomID);
            while (resultSet.next()) {
                amenities.add(mapResultSetToAmenity(resultSet));
            }
            return amenities;
        }
    }

    @Override
    public boolean isRoomAvailable(LocalDate checkInDate, LocalDate checkOutDate, int roomId) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            // using stored proecdure
            CallableStatement callableStatement = conn.prepareCall("{CALL sp_isRoomAvailable(?, ?, ?)}");
            callableStatement.setObject(1, checkInDate);
            callableStatement.setObject(2, checkOutDate);
            callableStatement.setInt(3, roomId);

            ResultSet resultSet = callableStatement.executeQuery();

            return resultSet.next();
        }
    }

    @Override
    public List<Room> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int roomTypeId, List<Integer> amenityIds) throws SQLException {
       try(Connection conn = DBConnection.getInstance().getConnection())
       {
           List<Room> rooms = new ArrayList<>();

           // using stored proecudre
           CallableStatement callableStatement = conn.prepareCall("{CALL sp_getAvailableRooms(?, ?, ?, ?)}");
           // setting up the params
           callableStatement.setObject(1, checkInDate);
           callableStatement.setObject(2, checkOutDate);
           callableStatement.setInt(3, roomTypeId);

           // only if amenities are passed, it will filter with amenities
           String amenityString = null;
           // seperating the ids by a comma
           if (amenityIds != null && !amenityIds.isEmpty()) {
               amenityString = amenityIds.stream().map(String::valueOf).collect(Collectors.joining(","));
           }

           // executing the query
           // if no amenities, it will not add amenityIds to it

               callableStatement.setString(4, amenityString);

           ResultSet resultSet = callableStatement.executeQuery();

           while (resultSet.next()) {
               // we want amenities
               List<Amenity> amenities = new ArrayList<>();
               String amenityName = resultSet.getString("amenities");

               // splitting the amenities into individual values
               if (amenityName != null && !amenityName.isEmpty()) {
                   String[] amenityNames = amenityName.split(",");
                   for (String name : amenityNames) {
                       Amenity amenity = new Amenity();
                       amenity.setName(name.trim());
                       amenities.add(amenity);
                   }
               }

               // we want to add room type to the result set too so
               RoomType roomType = new RoomType.RoomTypeBuilder().roomTypeId(resultSet.getInt("room_type_id"))
                       .roomTypeName(resultSet.getString("name"))
                       .basePricePerNight(resultSet.getDouble("price_per_night"))
                       .bedding(BeddingTypes.valueOf(resultSet.getString("bedding")))
                       .maxOccupancy(resultSet.getInt("max_occupancy"))
                       .totalRooms(resultSet.getInt("total_rooms"))
                       .amenityList(amenities)
                       .build();

               // we build a new one instead of using the mapResultSetToROom
               Room room = new Room.RoomBuilder().roomId(resultSet.getInt("room_id"))
                       .roomNum(resultSet.getString("room_number")).floorNum(resultSet.getInt("floor_num"))
                       .roomStatus(RoomStatus.valueOf(resultSet.getString("room_status"))).roomType(roomType).build();
               rooms.add(room);  // adding each room to the list
           }
       return rooms;  // returning all rooms
       }
    }

    // this is the mapping to room, based on table's column names
    private Room mapResultSetToRoom(ResultSet resultSet) throws SQLException {
        return new Room.RoomBuilder()
                .roomId(resultSet.getInt("room_id"))
                .roomNum(resultSet.getString("room_number"))
                .floorNum(resultSet.getInt("floor_num"))
                .roomStatus(RoomStatus.valueOf(resultSet.getString("room_status")))
                .build();
    }

    private RoomImg mapResultSetToRoomImg(ResultSet resultSet) throws SQLException {
        return new RoomImg(resultSet.getInt("image_id"),
                resultSet.getInt("room_type_id"),
                resultSet.getString("image_path"),
                resultSet.getString("alt"));
    }

    private Amenity mapResultSetToAmenity(ResultSet resultSet) throws SQLException {
        return new Amenity(resultSet.getInt("amenity_id"),
                resultSet.getString("name"),
                resultSet.getDouble("cost"));
    }

    private RoomType mapResultSetToRoomType(ResultSet rs) throws SQLException {
        return new RoomType.RoomTypeBuilder()
                .roomTypeId(rs.getInt("room_type_id"))
                .roomTypeName(rs.getString("name"))
                .basePricePerNight(rs.getDouble("price_per_night"))
                .baseDescription(rs.getString("description"))
                .bedding(BeddingTypes.valueOf(rs.getString("bedding")))
                .maxOccupancy(rs.getInt("max_occupancy"))
                .totalRooms(rs.getInt("total_rooms"))
                .roomImgList(new ArrayList<>())
                .amenityList(new ArrayList<>())
                .build();
    }
}
