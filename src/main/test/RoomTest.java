import business.service.impl.RoomServiceImpl;
import constant.RoomStatus;
import constant.RoomTypes;
import dto.RoomDTO;
import org.junit.*;
import java.sql.SQLException;
import java.util.*;

public class RoomTest {

    private static RoomServiceImpl roomService;

    @BeforeClass
    public static void init() throws SQLException {
        // initialize db connection & room service
        roomService = new RoomServiceImpl();
        // Optionally, populate test rooms
    }

    @Test
    public void testGetAll_NoFilters_ReturnsAllRooms() throws SQLException {
        List<RoomDTO> rooms = roomService.getAll(new HashMap<>());
        Assert.assertEquals(3, rooms.size());
    }

    @Test
    public void testGetAll_StatusFilter_ReturnsAvailableRooms() throws SQLException {
        Map<String, String> filters = new HashMap<>();
        filters.put("status", "Available");

        List<RoomDTO> rooms = roomService.getAll(filters);

        Assert.assertEquals(9, rooms.size());
        for (RoomDTO r : rooms) {
            Assert.assertEquals(RoomStatus.Available, rooms.get(0).getRoomStatus());
        }
    }

    @Test
    public void testGetAll_TypeFilter_ReturnsSingleRoom() throws SQLException {
        Map<String, String> filters = new HashMap<>();
        filters.put("room_type", "Standard");

        List<RoomDTO> rooms = roomService.getAll(filters);

        Assert.assertEquals(1, rooms.size());
        Assert.assertEquals(RoomTypes.Standard, rooms.get(0).getRoomType());
    }

    @Test
    public void testGetAll_MultipleFilters() throws SQLException {
        Map<String, String> filters = new HashMap<>();
        filters.put("room_type", "Standard");
        filters.put("status", "Available");

        List<RoomDTO> rooms = roomService.getAll(filters);

        Assert.assertEquals(1, rooms.size());
        Assert.assertEquals(RoomTypes.Standard, rooms.get(0).getRoomType());
        Assert.assertEquals(RoomStatus.Available, rooms.get(0).getRoomStatus());
    }

    @Test
    public void testGetAll_NoMatch_ReturnsEmptyList() throws SQLException {
        Map<String, String> filters = new HashMap<>();
        filters.put("room_type", "Suite");
        filters.put("status", "Unavailable");

        List<RoomDTO> rooms = roomService.getAll(filters);

        Assert.assertTrue(rooms.isEmpty());
    }

    @Test
    public void test_getAllRooms() throws SQLException {
        List<RoomDTO> rooms = roomService.getAll(new HashMap<>());
        Assert.assertEquals(5, rooms.size());
    }
}
