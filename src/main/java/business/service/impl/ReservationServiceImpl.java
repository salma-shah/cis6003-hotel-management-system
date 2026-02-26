package business.service.impl;

import business.service.*;
import business.service.decorators.*;
import constant.ReservationStatus;
import dto.*;
import entity.Reservation;
import exception.reservation.DuplicateResNumException;
import exception.reservation.ReservationNotFoundException;
import exception.room.RoomReservedException;
import exception.service.BusinessValidationException;
import exception.service.CheckOutDateBeforeCheckInException;
import exception.service.DateInPastException;
import mail.EmailBase;
import mail.EmailUtility;
import mail.factory.EmailFactory;
import mail.factory.impl.SuccessfulReservationEmailFactory;
import mapper.ReservationMapper;
import persistence.dao.ReservationDAO;
import persistence.dao.impl.ReservationDAOImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationServiceImpl implements ReservationService {
    private static final Logger LOG = Logger.getLogger(ReservationServiceImpl.class.getName());
    private final ReservationDAO reservationDAO;
    private final RoomService roomService;
    private final GuestService guestService;
    private final RoomTypeService roomTypeService;

    public ReservationServiceImpl() {
        this.reservationDAO = new ReservationDAOImpl();
        this.roomService = new RoomServiceImpl();
        this.guestService = new GuestServiceImpl();
        this.roomTypeService = new RoomTypeServiceImpl();
    }

    @Override
    public boolean update(ReservationDTO entity) {
        if (searchById(entity.getId()) == null) {
            throw new ReservationNotFoundException("Reservation ID : " + entity.getId() + "not found");
        }
        return reservationDAO.update(ReservationMapper.toReservation(entity));
    }

    @Override
    public boolean delete(int id)  {
        if (id <=0 )
        {
            throw new IllegalArgumentException("Invalid reservation ID");
        }
        if (existsByPrimaryKey(id)) {
            throw new ReservationNotFoundException("Reservation ID : " + id + " not found");
        }
       return reservationDAO.delete(id);
    }

    @Override
    public ReservationDTO searchById(int id)  {
        if (id <=0 )
        {
            throw new IllegalArgumentException("Invalid reservation ID");
        }

        Reservation reservation = reservationDAO.searchById(id);
        if (reservation == null)
        {
            throw new ReservationNotFoundException("Reservation ID : " + id + " not found");
        }
        return ReservationMapper.toReservationDTO(reservation);
    }

    @Override
    public List<ReservationDTO> getAll(Map<String, String> searchParams) {
        Map<String, String> filters = (searchParams != null) ? new HashMap<>(searchParams) : new HashMap<>();
        return ReservationMapper.toDTOList(reservationDAO.getAll(filters));
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey)  {
        if (primaryKey <=0 )
        {
            throw new IllegalArgumentException("Invalid reservation PK: " + primaryKey);
        }

        return reservationDAO.existsByPrimaryKey(primaryKey);

    }

    @Override
    public Integer findResIdByReservationNumber(String resNum)  {
        if (resNum == null)
        {
            throw new IllegalArgumentException("Invalid reservation number: " + resNum);
        }
        return reservationDAO.findReservationIdByReservationNumber(resNum);
    }

    @Override
    public boolean makeReservation(ReservationDTO reservationDTO, List<String> selectedAmenities)  {

        // checking if res num is unique
        validateResNum(reservationDTO.getReservationNumber());

             // checking if dates are valid
            if (!validateReservationDates(reservationDTO)) return false;

            // checking if room is eligible
//            int roomId = reservationDTO.getRoomId();
//            RoomDTO roomDTO = roomService.searchById(roomId);
//            if (!roomService.isRoomEligible(roomDTO, reservationDTO.getNumOfAdults(), reservationDTO.getNumOfChildren())) {return false;}

            // checking if room is available
            boolean isAvailable = roomService.isRoomAvailable(reservationDTO.getCheckInDate(), reservationDTO.getCheckOutDate(), reservationDTO.getRoomId());

            if (!isAvailable) {
                throw new RoomReservedException("The room is already reserved for these dates");
            }

            // calculating the total cost
            RoomTypeDTO roomTypeDTO = roomTypeService.getByRoomId(reservationDTO.getRoomId());
            double basePrice = roomTypeDTO.getBasePricePerNight();
            // using the calculate total cost method
            double totalCost = calculateTotalCostForStay(basePrice, reservationDTO.getCheckInDate(), reservationDTO.getCheckOutDate(), selectedAmenities);

            if (totalCost < 0)
            {
                throw new BusinessValidationException("The total cost for a reservation cannot be less than 0");
            }

            ReservationDTO updatedDTO = new ReservationDTO(
                    reservationDTO.getId(),
                    reservationDTO.getGuestId(),
                    reservationDTO.getRoomId(),
                    reservationDTO.getReservationNumber(),
                    totalCost,
                    LocalDateTime.now(),
                    reservationDTO.getCheckInDate(),
                    reservationDTO.getCheckOutDate(),
                    reservationDTO.getNumOfAdults(),
                    reservationDTO.getNumOfChildren(),
                    ReservationStatus.Confirmed
            );

            Reservation reservation = ReservationMapper.toReservation(updatedDTO);
            LOG.log(Level.INFO, "Reservation has been made: {0}", reservation);

            boolean successfulReservation = reservationDAO.add(reservation);
            if (!successfulReservation)
            {
               return false;
            }
            // sending email is successful
            sendSuccessfulResEmail(reservationDTO);
            return true;
    }

    @Override
    public boolean validateReservationDates(ReservationDTO reservationDTO)  {
        LocalDate localDate = LocalDate.now();
        // checking if dates are in the past
        if (reservationDTO.getCheckInDate().isBefore(localDate) || reservationDTO.getCheckOutDate().isBefore(localDate))
        {
            throw new DateInPastException("Reservation dates cannot be in the past");
        }

        if (reservationDTO.getCheckInDate().isAfter(reservationDTO.getCheckOutDate())) {
           throw new CheckOutDateBeforeCheckInException("The checkout date cannot be before checkin date");
        }
        return true;
    }

    public boolean validateResNum(String resNum)  {
        if (resNum == null || resNum.isEmpty())
        {
            throw new IllegalArgumentException("Invalid reservation number");
        }

        boolean existingResNum = reservationDAO.validateReservationNumber(resNum);
        if (existingResNum)
        {
            throw new DuplicateResNumException("The reservation number " + resNum + " already exists");
        }
        return true;
    }

    @Override
    public void sendSuccessfulResEmail(ReservationDTO reservationDTO)  {

        if (reservationDTO == null)
        {
            throw new IllegalArgumentException("Invalid reservation DTO");
        }
        // getting the required params for email
        GuestDTO guestDTO = guestService.searchById(reservationDTO.getGuestId());
        String roomTypeName = roomTypeService.getByRoomId(reservationDTO.getRoomId()).getRoomTypeName();
        // using the factory method for succ res email
        EmailFactory emailFactory = new SuccessfulReservationEmailFactory(reservationDTO, guestDTO, roomTypeName);
        // then using the base interface
        EmailBase emailBase = emailFactory.createEmail();
        // then finally the send mail utility
        EmailUtility.sendMail(emailBase.getReceiver(), emailBase.getSubject(), emailBase.getBody());

    }

    @Override
    public double calculateTotalCostForStay(double basePricePerNight, LocalDate startDate, LocalDate endDate, List<String> selectedAmenities)  {
        LocalDate localDate = LocalDate.now();
        if (basePricePerNight < 0)
        {
            throw new IllegalArgumentException("Invalid base price per night");
        }

        if (endDate.isBefore(startDate))
        {
            throw new CheckOutDateBeforeCheckInException("The checkout date cannot before the checkin date");
        }

        if (startDate.isBefore(localDate) ||  endDate.isBefore(localDate))
        {
            throw new DateInPastException("The reservation dates cannot be in the past");
        }

        long numOfNights = ChronoUnit.DAYS.between(startDate, endDate);  // calculating the number of nights stayed
        if (numOfNights < 0) return 0;
        IRoom room = new BasicRoom(basePricePerNight);

        // based on the amenity, you calculate the cost
        for (String key : selectedAmenities) {
            switch (key) {
                case "1" -> room = new WifiDecorator(room);
                case "2" -> room = new PoolDecorator(room);
                case "3" -> room = new BathtubDecorator(room);
                case "4" -> room = new BalconyDecorator(room);
                case "5" -> room = new BreakfastDecorator(room);
                case "6" -> room = new SpaDecorator(room);
                case "7" -> room = new IronboardDecorator(room);
                case "8" -> room = new HammockDecorator(room);
                case "9" -> room = new MinibarDecorator(room);
                case "10" -> room = new GiftpackDecorator(room);
            }
        }
        return room.getCost() * numOfNights;
    }

    @Override
    public ReservationDTO getByReservationNumber(String resNum)  {
        if (resNum == null || resNum.isEmpty())
        {
            throw new IllegalArgumentException("Invalid reservation number");
        }

        Reservation reservation = reservationDAO.findByReservationNumber(resNum);
        if (reservation == null)
        {
            throw new ReservationNotFoundException("Reservation not found");
        }

        return ReservationMapper.toReservationDTO(reservation);
    }

    @Override
    public ReservationAggregateDTO getFullReservation(int id)  {
        if (id <= 0)
        {
            throw new IllegalArgumentException("Invalid reservation id");
        }
            return reservationDAO.findFullReservation(id);
    }

    @Override
    public boolean updateReservationStatus(int id, ReservationStatus status)  {

        if (id <= 0)
        {
            throw new IllegalArgumentException("Invalid reservation id");
        }
        if (status != ReservationStatus.CheckedOut && status != ReservationStatus.CheckedIn
        && status != ReservationStatus.Cancelled)
        {
            throw new IllegalArgumentException("Invalid reservation status");
        }

            return reservationDAO.updateReservationStatus(id, status);
    }
}
