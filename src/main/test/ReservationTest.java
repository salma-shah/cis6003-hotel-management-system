import business.service.ReservationService;
import business.service.impl.ReservationServiceImpl;
import dto.ReservationDTO;
import org.junit.Test;
import security.ReservationIdGenerator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static constant.ReservationStatus.Confirmed;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReservationTest {

//    @Test
//    public void test_makeReservationWithUniqueReservationNum() throws SQLException {
//        ReservationIdGenerator reservationIdGenerator = new ReservationIdGenerator();
//        ReservationService reservationService = new ReservationServiceImpl();
//
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        LocalDate checkinDate = LocalDate.of(2026, Month.AUGUST, 29);
//        LocalDate checkoutDate = LocalDate.of(2026, Month.AUGUST, 30);
//        ReservationDTO reservationDTO = new ReservationDTO(6, 3, 2, reservationIdGenerator.generateId(),
//                22500.00, currentDateTime, checkinDate, checkoutDate, 2, 1, Confirmed);
//
//        boolean result = reservationService.makeReservation(reservationDTO);
//        assertTrue(result);
//    }
//
//    @Test
//    public void test_makeReservationWithDuplicateReservationNum() throws SQLException {
//        ReservationIdGenerator reservationIdGenerator = new ReservationIdGenerator();
//        ReservationService reservationService = new ReservationServiceImpl();
//
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        LocalDate checkinDate = LocalDate.of(2026, Month.MARCH, 9);
//        LocalDate checkoutDate = LocalDate.of(2026, Month.MARCH, 11);
//        ReservationDTO reservationDTO = new ReservationDTO(2, 3, 2, "RV58e74169-b998-4u4c-a015-21dd505b44a5",
//                22500.00, currentDateTime, checkinDate, checkoutDate, 2, 1, Confirmed);
//
//        boolean result = reservationService.makeReservation(reservationDTO);
//        assertFalse(result);
//    }
//
//    @Test
//    public void test_makeReservationForUnavailableRoom() throws SQLException {
//        ReservationService reservationService = new ReservationServiceImpl();
//        LocalDate checkinDate = LocalDate.of(2026, Month.MARCH, 9);
//        LocalDate checkoutDate = LocalDate.of(2026, Month.MARCH, 11);
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        ReservationDTO reservationDTO = new ReservationDTO(2, 3, 2, "RV58e74169-b998-4u4c-a015-21dd505b44a5",
//                22500.00, currentDateTime, checkinDate, checkoutDate, 2, 1, Confirmed);
//
//        boolean successfulRes = reservationService.makeReservation(reservationDTO);
//        assertFalse("The room is already marked for reservation",successfulRes);
//
//    }
//
//    @Test
//    public void test_makeReservationForAvailableRoom() throws SQLException {
//        ReservationService reservationService = new ReservationServiceImpl();
//        ReservationIdGenerator reservationIdGenerator = new ReservationIdGenerator();
//        LocalDate checkinDate = LocalDate.of(2026, Month.MAY, 28);
//        LocalDate checkoutDate = LocalDate.of(2026, Month.MAY, 30);
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        ReservationDTO reservationDTO = new ReservationDTO(7, 3, 6, reservationIdGenerator.generateId(),
//                45700.00, currentDateTime, checkinDate, checkoutDate, 2, 1, Confirmed);
//
//        boolean successfulRes = reservationService.makeReservation(reservationDTO);
//        assertTrue(successfulRes);
//    }
//
//    @Test
//    public void test_unsuccessfulReservationForDatesInThePast() throws SQLException
//    {
//        ReservationService reservationService = new ReservationServiceImpl();
//        ReservationIdGenerator reservationIdGenerator = new ReservationIdGenerator();
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        LocalDate checkinDate = LocalDate.of(2026, Month.JANUARY, 9);
//        LocalDate checkoutDate = LocalDate.of(2026, Month.JANUARY, 11);
//        ReservationDTO reservationDTO  = new ReservationDTO(3, 3, 6, reservationIdGenerator.generateId(),
//                45700.00, currentDateTime, checkinDate, checkoutDate, 2, 1, Confirmed);
//
//        boolean successfulRes = reservationService.makeReservation(reservationDTO);
//        assertFalse("The dates cannot be in the past", successfulRes);
//    }
//
//    @Test
//    public void test_unsuccessfulReservationForCheckinDateBeforeCheckoutDate() throws SQLException {
//        ReservationService reservationService = new ReservationServiceImpl();
//        ReservationIdGenerator reservationIdGenerator = new ReservationIdGenerator();
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        LocalDate checkinDate = LocalDate.of(2026, Month.APRIL, 19);
//        LocalDate checkoutDate = LocalDate.of(2026, Month.APRIL, 14);
//        ReservationDTO reservationDTO =  new ReservationDTO(3, 3, 4, reservationIdGenerator.generateId(),
//                45700.00, currentDateTime, checkinDate, checkoutDate, 2, 1, Confirmed);
//
//        boolean successfulRes = reservationService.makeReservation(reservationDTO);
//        assertFalse("Checkin date cannot be after checkout date", successfulRes);
//    }
//


}
