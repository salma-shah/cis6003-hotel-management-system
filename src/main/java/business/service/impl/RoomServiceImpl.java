package business.service.impl;

import business.service.RoomService;
import dto.AmenityDTO;
import dto.RoomDTO;
import entity.Room;
import mapper.RoomMapper;
import persistence.dao.RoomDAO;
import persistence.dao.impl.RoomDAOImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RoomServiceImpl implements RoomService {
    private final RoomDAO roomDAO;
  //  private final List<Room> rooms = new ArrayList<>();
    private static final Logger LOG = Logger.getLogger(RoomServiceImpl.class.getName());

    public RoomServiceImpl() {
        this.roomDAO = new RoomDAOImpl();
    }

    public boolean add(RoomDTO roomDTO) throws SQLException {
        try
        {
            if (roomDAO.existsByPrimaryKey( roomDTO.getRoomId()))
            {
                return false; // room already exists
            }
            // otherwise, room will be added
            Room roomEntity = RoomMapper.toRoom(roomDTO);
            LOG.log(Level.INFO, "The room : " + roomEntity + " was successfully added.");
            return roomDAO.add(roomEntity);
        }
        catch(SQLException ex)
        {
            LOG.severe("Error adding room: " + ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }

    public int addAndReturnId(RoomDTO roomDTO) throws SQLException {

        Room roomEntity = RoomMapper.toRoom(roomDTO);

        if (roomDAO.add(roomEntity)) {
            return roomDAO.getLastInsertedId();
        }
        return 0;
    }
    @Override
    public boolean update(RoomDTO roomDTO) throws SQLException {
       // checking for existing room
        try {

            if (!roomDAO.existsByPrimaryKey(roomDTO.getRoomId())) {
                return false;
            }

            Room room = RoomMapper.toRoom(roomDTO);

            return roomDAO.update(room);
        }
        catch (SQLException ex)
        {
            LOG.severe("Error updating room: " + ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
        }

    @Override
    public boolean delete(int id) throws SQLException {
        try {
            // if the user for the specific PK exists, delete the user
            if (roomDAO.existsByPrimaryKey( id))
            {
                return roomDAO.delete(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public RoomDTO searchById(int id) throws SQLException {
        try{
            return RoomMapper.toRoomDTO(roomDAO.searchById(id));
        }
        catch (SQLException ex)
        {
            LOG.severe("Error searching room: " + ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey) throws SQLException {
        try
        {
            return roomDAO.existsByPrimaryKey( primaryKey);
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
        try
        {
            // room parameters
            Map<String, String> filters = (searchParams!= null) ? new HashMap<>(searchParams) : new HashMap<>();
            List<RoomDTO> rooms = RoomMapper.toRoomDTOList(roomDAO.getAll(filters));
            LOG.log(Level.INFO, "The rooms : " + rooms);

            // checking if amenities exist
            // we will return it to the DAO by splitting it
            if (filters.containsKey("amenityId")) {
                String[] amenityIds = filters.get("amenityId").split(",");
                Set<String> amenities = new HashSet<>(Arrays.asList(amenityIds));
                rooms = rooms.stream().filter(room -> {
                    List<AmenityDTO> roomAmenities = room.getRoomType().getAmenityList();
                    if (roomAmenities == null || roomAmenities.isEmpty()) return false;

                    Set<String> roomAmenityIds = new HashSet<>();
                    for (AmenityDTO a : roomAmenities) {
                        roomAmenityIds.add(String.valueOf(a.getId()));
                    }
                    return roomAmenityIds.containsAll(amenities);
                })
                        .collect(Collectors.toList());

            }
            return rooms;
       }
        catch (SQLException ex)
        {
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean isRoomEligible(RoomDTO roomDTO, int adults, int children) throws SQLException {
        int totalGuests = adults + children;
        int maxOccupancy = roomDTO.getRoomType().getMaxOccupancy();

        // if total guests itself is higher than the max occupancy for a room, not eligible
        if (totalGuests > maxOccupancy)
        {return false;}

        // then for children, they can only stay in rooms with a double or king bed
        if (children > 0)
        {
            String bedding = roomDTO.getRoomType().getBedding().name();
            if (!bedding.contains("Double") && !bedding.contains("King"))
            { return false;}
        }
        // if all room eligibity if fulfiled, return true
        return true;
    }

    @Override
    public List<RoomDTO> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int roomTypeId, List<Integer> amenityIds) throws SQLException {
        LocalDate localDate = LocalDate.now();

        // checking if dates are in the past
        if (checkInDate.isBefore(localDate) || checkOutDate.isBefore(localDate))
        {
            LOG.log(Level.INFO, "The dates cannot be in the past");
            return null;
        }

        if (checkInDate.isAfter(checkOutDate))
        {
            LOG.log(Level.INFO, "Checkin date cannot be after checkout date");
            return null;
        }
        return RoomMapper.toRoomDTOList(roomDAO.findAvailableRooms(checkInDate, checkOutDate, roomTypeId, amenityIds));
    }

    @Override
    public boolean isRoomAvailable(LocalDate checkInDate, LocalDate checkOutDate, int roomId) throws SQLException {
        // first we query the database to check if there are any overlapping reservations on the same dates
        return roomDAO.isRoomAvailable(checkInDate, checkOutDate, roomId);
        //  return reservedRooms.isEmpty();   // if the list is empty, it means the room is avaialble ; so it is true
    }

}
