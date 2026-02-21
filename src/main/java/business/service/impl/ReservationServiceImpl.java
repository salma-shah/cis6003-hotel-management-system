package business.service.impl;

import business.service.*;
import business.service.decorators.*;
import constant.ReservationStatus;
import dto.GuestDTO;
import dto.ReservationDTO;
import dto.RoomTypeDTO;
import entity.Reservation;
import mail.EmailBase;
import mail.EmailUtility;
import mail.factory.EmailFactory;
import mail.factory.impl.SuccessfulReservationEmailFactory;
import mapper.ReservationMapper;
import persistence.dao.ReservationDAO;
import persistence.dao.impl.ReservationDAOImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public boolean update(ReservationDTO entity) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        return false;
    }

    @Override
    public ReservationDTO searchById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<ReservationDTO> getAll(Map<String, String> searchParams) throws SQLException {
        return List.of();
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey) throws SQLException {
        return false;
    }

    @Override
    public boolean makeReservation(ReservationDTO reservationDTO, List<String> selectedAmenities) throws SQLException {
        try {
            // checking if res num is unique
            if (validateResNum(reservationDTO.getReservationNumber())) {
                LOG.log(Level.WARNING, "Reservation number already exists.");
                return false; }

             if (reservationDAO.searchById(reservationDTO.getId()) != null) {return false;}

             // checking if dates are valid
            if (!validateReservationDates(reservationDTO)) return false;
            // checking if room is available
            boolean isAvailable = roomService.isRoomAvailable(reservationDTO.getCheckInDate(), reservationDTO.getCheckOutDate(), reservationDTO.getRoomId());

            if (!isAvailable) {
                LOG.log(Level.INFO, "The room is already marked for reservation");
                return false;
            }

            // calculating the total cost
            RoomTypeDTO roomTypeDTO = roomTypeService.getByRoomId(reservationDTO.getRoomId());
            double basePrice = roomTypeDTO.getBasePricePerNight();
            // using the calculate total cost method
            double totalCost = calculateTotalCostForStay(basePrice, reservationDTO.getCheckInDate(), reservationDTO.getCheckOutDate(), selectedAmenities);

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
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error making reservation in service layer", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean validateReservationDates(ReservationDTO reservationDTO) throws SQLException {
        LocalDate localDate = LocalDate.now();

        // checking if dates are in the past
        if (reservationDTO.getCheckInDate().isBefore(localDate) || reservationDTO.getCheckOutDate().isBefore(localDate))
        {
            LOG.log(Level.INFO, "The dates cannot be in the past");
            return false;
        }

        if (reservationDTO.getCheckInDate().isAfter(reservationDTO.getCheckOutDate())) {
            LOG.log(Level.INFO, "Checkin date cannot be after checkout date");
            return false;
        }
        return true;
    }

    public boolean validateResNum(String resNum) throws SQLException {
        return reservationDAO.validateReservationNumber(resNum);
    }

    @Override
    public void sendSuccessfulResEmail(ReservationDTO reservationDTO) throws SQLException {

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
    public double calculateTotalCostForStay(double basePricePerNight, LocalDate startDate, LocalDate endDate, List<String> selectedAmenities) throws SQLException {
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
}
