package persistence.dao.impl;

import constant.BeddingTypes;
import db.DBConnection;
import entity.RoomType;
import exception.db.DataAccessException;
import exception.EntityNotFoundException;
import persistence.dao.RoomTypeDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomTypeDAOImpl implements RoomTypeDAO {

    @Override
    public boolean add(RoomType entity)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(conn, "INSERT INTO room_type (name, price_per_night, description, " +
                            " bedding, max_occupancy, total_rooms) VALUES (?,?,?,?,?,?)", entity.getRoomTypeName(), entity.getBasePricePerNight(),
                    entity.getBaseDescription(), entity.getBedding(), entity.getMaxOccupancy(), entity.getTotalRooms()
                    );
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error adding room type:", e);
        }
    }

    @Override
    public boolean update(RoomType entity)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(conn, "UPDATE room_type SET name=?, price_per_night=?, description=?, bedding=?, total_rooms=? WHERE room_type_id =? ",
            entity.getRoomTypeName(),entity.getBasePricePerNight(), entity.getBaseDescription(), entity.getBedding(), entity.getTotalRooms(), entity.getRoomTypeId());
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error updating room type:", e);
        }
    }

    @Override
    public boolean delete(int id)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(conn, "DELETE FROM room_type WHERE room_type_id=? ",id);
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error deleting room type:", e);
        }
    }

    @Override
    public RoomType searchById(int id)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(conn, "SELECT * FROM room_type WHERE room_type_id=? ",
                    rs -> {
                if (rs.next()) {
                    return mapResultSetToRoomType(rs);
                }
                return null;
                    }, id);
        }
        catch (SQLException e)
        {
            throw new EntityNotFoundException("Error finding room type with ID:" + id, e);
        }
    }

    @Override
    public RoomType findByRoomId(int roomId)  {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT rt.* FROM room r JOIN room_type rt ON r.room_type_id= rt.room_type_id WHERE r.room_id=?",
                    rs -> {

                if (rs.next()) {
                    return mapResultSetToRoomType(rs);
                }
                return null;
                    },
                    roomId);
        }
        catch (SQLException e)
        {
            throw new EntityNotFoundException("Error finding room type with room ID:" + roomId, e);
        }
    }

    @Override
    public List<RoomType> getAll(Map<String, String> searchParams)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            String sql = "SELECT * FROM room_type";
            List<RoomType> list = new ArrayList<>();

            return QueryHelper.executeQuery(conn, sql,
                    rs -> {
                        while (rs.next()) {
                            list.add(mapResultSetToRoomType(rs));
                        }
                        return list;
                    });
        }
        catch (SQLException e)
        {
            throw new EntityNotFoundException("Error finding room types", e);
        }
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey)  {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(conn, "SELECT 1 FROM room_type WHERE room_type_id=? ",
                    rs -> rs.next(), primaryKey);
        }
        catch (SQLException e)
        {
            throw new EntityNotFoundException("Error finding room type with primary key:" + primaryKey, e);
        }
    }

    private RoomType mapResultSetToRoomType(ResultSet rs)  {
        try {
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
        catch (SQLException e)
        {
            throw new EntityNotFoundException("Error mapping to room type:", e);
        }
    }
}
