package persistence.dao.impl;

import constant.BeddingTypes;
import db.DBConnection;
import entity.RoomType;
import persistence.dao.RoomTypeDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomTypeDAOImpl implements RoomTypeDAO {
    Logger LOG =  Logger.getLogger(RoomTypeDAOImpl.class.getName());

    @Override
    public boolean add(RoomType entity) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.execute(conn, "INSERT INTO room_type (name, price_per_night, description, " +
                            "             bedding, max_occupancy, total_rooms) VALUES (?,?,?,?,?,?)", entity.getRoomTypeName(), entity.getBasePricePerNight(),
                    entity.getBaseDescription(), entity.getBedding(), entity.getMaxOccupancy(), entity.getTotalRooms()
                    );
        }
        catch (SQLException e)
        {
            LOG.log(Level.INFO, "Something went wrong while trying to add room type in DAO");
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public boolean update(RoomType entity) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.execute(conn, "UPDATE room_type SET name=?, price_per_night=?, description=?, bedding=?, total_rooms=? WHERE room_type_id =? ",
            entity.getRoomTypeName(),entity.getBasePricePerNight(), entity.getBaseDescription(), entity.getBedding(), entity.getTotalRooms(), entity.getRoomTypeId());
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.execute(conn, "DELETE FROM room_type WHERE room_type_id=? ",id);
        }
    }

    @Override
    public RoomType searchById(int id) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet = QueryHelper.execute(conn, "SELECT * FROM room_type WHERE room_type_id=? ",id);
            if (!resultSet.next())
            {
                return null;
            }
            return mapResultSetToRoomType(resultSet);
        }
    }

    @Override
    public RoomType findByRoomId(int roomId) throws SQLException {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet = QueryHelper.execute(connection, "SELECT rt.* FROM room r JOIN room_type rt ON r.room_type_id= rt.room_type_id WHERE r.room_id=?",roomId);
            if (!resultSet.next())
                {
                return null;
                }
            return mapResultSetToRoomType(resultSet);
        }
    }

    @Override
    public List<RoomType> getAll(Map<String, String> searchParams) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            String sql = "SELECT * FROM room_type";
            List<RoomType> list = new ArrayList<>();

            ResultSet resultSet = QueryHelper.execute(conn, sql);
            while (resultSet.next())
            {
                list.add(mapResultSetToRoomType(resultSet));
            }
            return list;
        }
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            ResultSet resultSet = QueryHelper.execute(conn, "SELECT 1 FROM room_type WHERE room_type_id=? ",primaryKey);
            return resultSet.next();
        }
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
                .build();
    }
}
