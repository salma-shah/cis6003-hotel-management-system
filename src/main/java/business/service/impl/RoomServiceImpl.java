package business.service.impl;

import business.service.RoomService;
import business.service.RoomTypeService;
import dto.AmenityDTO;
import dto.RoomDTO;
import dto.RoomTypeDTO;
import entity.Room;
import exception.room.DuplicateRoomNumberException;
import exception.room.RoomNotFoundException;
import exception.service.BusinessValidationException;
import exception.service.CheckOutDateBeforeCheckInException;
import exception.service.DateInPastException;
import mapper.RoomMapper;
import persistence.dao.RoomDAO;
import persistence.dao.impl.RoomDAOImpl;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RoomServiceImpl implements RoomService {
    private final RoomDAO roomDAO;
    private final RoomTypeService typeService;
  //  private final List<Room> rooms = new ArrayList<>();
    private static final Logger LOG = Logger.getLogger(RoomServiceImpl.class.getName());
    private final Set<String> roomNumbers = ConcurrentHashMap.newKeySet();

    public RoomServiceImpl() {
        this.roomDAO = new RoomDAOImpl();
        this.typeService = new RoomTypeServiceImpl();
        preloadUniqueFields();  // preloading the room numbers to ensure no duplicate room numbers while saving a room
    }

    private void preloadUniqueFields() {
        List<RoomDTO> roomDTOS = new ArrayList<>();

        // lambda function
        roomDTOS.forEach(roomDTO -> {
            roomNumbers.add(roomDTO.getRoomNum());
        });
    }


    public boolean add(RoomDTO roomDTO) {
        if (!roomNumbers.add(roomDTO.getRoomNum())) {
            throw new DuplicateRoomNumberException("The room number " + roomDTO.getRoomNum() + " is already in use");
        }

        // otherwise, room will be added
        Room roomEntity = RoomMapper.toRoom(roomDTO);
        LOG.log(Level.INFO, "The room : " + roomEntity + " was successfully added.");
        return roomDAO.add(roomEntity);

    }

    public int addAndReturnId(RoomDTO roomDTO)  {
        Room roomEntity = RoomMapper.toRoom(roomDTO);
        if (roomDAO.add(roomEntity)) { return roomDAO.getLastInsertedId();}
        return 0;
    }

    @Override
    public boolean existsByRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.isEmpty()) {
            throw new IllegalArgumentException("The room number is invalid");
        }
        return roomDAO.existsByRoomNum(roomNumber);
    }

    @Override
    public boolean update(RoomDTO roomDTO)  {
       // checking for existing room
        if (!existsByPrimaryKey(roomDTO.getRoomId())) {return false;}
        Room room = RoomMapper.toRoom(roomDTO);

        if (room == null) {throw new RoomNotFoundException("The room was not found");}
        return roomDAO.update(room);
    }

    @Override
    public boolean delete(int id)  {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid room ID.");
        }
        // if the room for the specific PK exists, delete the user
        if (!existsByPrimaryKey(id)) {
            return false;
        }
        return roomDAO.delete(id);
    }

    @Override
    public RoomDTO searchById(int id)  {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid room ID.");
        }

        Room room = roomDAO.searchById(id);
        if (room == null) {
            throw new RoomNotFoundException("The room ID " + id + " was not found");
        }
        return RoomMapper.toRoomDTO(room);

    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey)  {

        if (primaryKey <= 0) {
            throw new IllegalArgumentException("Invalid room PK.");
        }
        return (roomDAO.existsByPrimaryKey(primaryKey));
    }

    // this gets all rooms With filters
    // if no filters, it gets all rooms without filters
    @Override
    public List<RoomDTO> getAll(Map<String, String> searchParams)  {

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

    @Override
    public boolean isRoomEligible(RoomDTO roomDTO, int adults, int children)  {

        if (adults <= 0 && children < 0) {
            throw new IllegalArgumentException("Invalid value of adults or children.");
        }

        int totalGuests = adults + children;
        LOG.log(Level.INFO, "The  : " + roomDTO);
        RoomTypeDTO roomTypeDTO =typeService.getByRoomId(roomDTO.getRoomId());
        LOG.log(Level.INFO, "The roomType : " + roomTypeDTO);
        int maxOccupancy = roomTypeDTO.getMaxOccupancy();
        LOG.log(Level.INFO, "The maxOccupancy : " + maxOccupancy);

        // if total guests itself is higher than the max occupancy for a room, not eligible
        if (totalGuests > maxOccupancy)
        { throw new BusinessValidationException("The maximum number of guests is " + maxOccupancy + "."); }

        // then for children, they can only stay in rooms with a double or king bed
        if (children > 0)
        {
            String bedding = roomDTO.getRoomType().getBedding().name();
            if (!bedding.contains("Double") && !bedding.contains("King"))
            {
               throw new BusinessValidationException("Only rooms with double and king bedding support guests with children");
            }
        }
        // if all room eligibility if fulfilled, return true
        return true;
    }

    @Override
    public List<RoomDTO> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int roomTypeId, List<Integer> amenityIds)  {

        if (roomTypeId <= 0 )
        {
            throw new IllegalArgumentException("Invalid room type.");
        }

        LocalDate localDate = LocalDate.now();
        // checking if dates are in the past
        if (checkInDate.isBefore(localDate) || checkOutDate.isBefore(localDate))
        {
            throw new DateInPastException("Dates cannot be in the past.");
        }

        if (checkInDate.isAfter(checkOutDate))
        {
            throw new CheckOutDateBeforeCheckInException("The check in date has to be before the check out date.");
        }
        return RoomMapper.toRoomDTOList(roomDAO.findAvailableRooms(checkInDate, checkOutDate, roomTypeId, amenityIds));
    }

    @Override
    public boolean isRoomAvailable(LocalDate checkInDate, LocalDate checkOutDate, int roomId)  {
        // we query the database to check if there are any overlapping reservations on the same dates
        if (roomId <= 0) {
            throw new IllegalArgumentException("Invalid room ID.");
        }
        LocalDate localDate = LocalDate.now();
        // checking if dates are in the past
        if (checkInDate.isBefore(localDate) || checkOutDate.isBefore(localDate))
        {
            throw new DateInPastException("Dates cannot be in the past.");
        }

        if (checkInDate.isAfter(checkOutDate))
        {
            throw new CheckOutDateBeforeCheckInException("The check in date has to be before the check out date.");
        }

        return roomDAO.isRoomAvailable(checkInDate, checkOutDate, roomId);
    }

}
