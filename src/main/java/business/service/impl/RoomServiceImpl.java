package business.service.impl;

import business.service.RoomService;
import db.DBConnection;
import dto.RoomDTO;
import entity.Room;
import mapper.RoomMapper;
import persistence.dao.RoomDAO;
import persistence.dao.impl.RoomDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomServiceImpl implements RoomService {
    private final RoomDAO roomDAO;
    private final List<Room> rooms = new ArrayList<>();
    private static final Logger LOG = Logger.getLogger(RoomServiceImpl.class.getName());
    // private final Connection connection = DBConnection.getInstance().getConnection();

    public RoomServiceImpl() {
        this.roomDAO = new RoomDAOImpl();
    }

    public boolean add(RoomDTO roomDTO) throws SQLException {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            if (roomDAO.existsByPrimaryKey(connection, roomDTO.getRoomId()))
            {
                return false; // room already exists
            }
            // otherwise, room will be added
            Room roomEntity = RoomMapper.toRoom(roomDTO);
            LOG.log(Level.INFO, "The room : " + roomEntity + " was successfully added.");
            return roomDAO.add(connection, roomEntity);
        }
        catch(SQLException ex)
        {
            LOG.severe("Error adding room: " + ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }

    public int addAndReturnId(RoomDTO roomDTO) throws SQLException {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            Room roomEntity = RoomMapper.toRoom(roomDTO);
            boolean created = roomDAO.add(conn, roomEntity);
            if (created) {
                return roomDAO.getLastInsertedId(conn); // same connection
            } else {
                return 0; // or throw exception
            }
        }
    }

    @Override
    public boolean update(RoomDTO roomDTO) throws SQLException {
       // checking for existing room
        try (Connection conn = DBConnection.getInstance().getConnection()) {

            if (!roomDAO.existsByPrimaryKey(conn, roomDTO.getRoomId())) {
                return false;
            }

            Room room = RoomMapper.toRoom(roomDTO);

            return roomDAO.update(conn, room);
        }
        catch (SQLException ex)
        {
            LOG.severe("Error updating room: " + ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
        }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            // if the user for the specific PK exists, delete the user
            if (roomDAO.existsByPrimaryKey(connection, id))
            {
                return roomDAO.delete(connection, id);
            }
        }
        return false;
    }

    @Override
    public RoomDTO searchById(int id) throws SQLException {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return RoomMapper.toRoomDTO(roomDAO.searchById(connection, id));
        }
        catch (SQLException ex)
        {
            LOG.severe("Error searching room: " + ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey) throws SQLException {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return roomDAO.existsByPrimaryKey(connection, primaryKey);
        }
        catch (Exception ex)
        {
            throw new SQLException(ex.getMessage());
        }
    }


    // this gets all rooms With filters
    // if no filters, it gets all rooms without filters
    @Override
    public List<RoomDTO> getAll(Map<String, String> searchParams) throws SQLException {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            // room parameters
            Map<String, String> filters = (searchParams!= null) ? new HashMap<>(searchParams) : new HashMap<>();

            return RoomMapper.toRoomDTOList(roomDAO.getAll(connection, filters));
       }
        catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage());
        }
    }

//    @Override
//    public IRoom getRoomWithPricing(int roomId) {
//        try(Connection connection = DBConnection.getInstance().getConnection())
//        {
////            Room roomEntity = roomDAO.searchById(connection, roomId);
////            IRoom room = new BasicRoom();
//
//            // this is room with a pool
//
//        }
//        catch (SQLException ex)
//        {
//         //   throw new SQLException(ex.getMessage());
//        }
//        return null;
//    }

}
