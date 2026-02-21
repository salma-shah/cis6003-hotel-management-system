import business.service.RoomService;
import business.service.impl.RoomServiceImpl;
import dto.RoomDTO;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RoomAvailabilityTest {
    @Test
    public void test_getUnAvailableRooms() throws SQLException {
        RoomService roomService = new RoomServiceImpl();
        LocalDate checkInDate = LocalDate.of(2026, Month.MARCH, 19);
        LocalDate checkOutDate = LocalDate.of(2026, Month.MARCH, 21);
        boolean isAvailable = roomService.isRoomAvailable(checkInDate, checkOutDate, 2);
        assertFalse(isAvailable);   //  it should not be avaialble because room ID 2 is reserved on those dates
    }

    @Test
    public void test_getAvailableRooms() throws SQLException {
        RoomService roomService = new RoomServiceImpl();
        LocalDate checkInDate = LocalDate.of(2026, Month.APRIL, 19);
        LocalDate checkOutDate = LocalDate.of(2026, Month.APRIL, 21);
        boolean isAvailable = roomService.isRoomAvailable(checkInDate, checkOutDate, 2);
        assertTrue(isAvailable);
    }

    @Test
    public void test_getAvailableRoomsForDatesAndAmenities() throws SQLException {
        RoomService roomService = new RoomServiceImpl();
        LocalDate checkInDate = LocalDate.of(2026, Month.DECEMBER, 19);
        LocalDate checkOutDate = LocalDate.of(2026, Month.DECEMBER, 21);

        Integer[] amenityParams = {1, 2, 5};
        List<Integer> amenityIds = new ArrayList<>(Arrays.asList(amenityParams));

        List<RoomDTO> roomDTOList = roomService.findAvailableRooms(checkInDate, checkOutDate, 2, amenityIds);
        System.out.println(roomDTOList.size());
    }
}
