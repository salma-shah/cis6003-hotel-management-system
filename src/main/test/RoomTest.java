import business.service.impl.RoomServiceImpl;
import constant.RoomStatus;
import dto.RoomDTO;
import org.junit.*;

import java.util.*;

public class RoomTest {

    private static RoomServiceImpl roomService;

    @BeforeClass
    public static void init()  {
        // initialize db connection & room service
        roomService = new RoomServiceImpl();
        // Optionally, populate test rooms
    }

    @Test
    public void testGetAll_NoFilters_ReturnsAllRooms()  {
        List<RoomDTO> rooms = roomService.getAll(new HashMap<>());
        Assert.assertEquals(12, rooms.size());
    }

    @Test
    public void testGetAll_StatusFilter_ReturnsAvailableRooms()  {
        Map<String, String> filters = new HashMap<>();
        filters.put("status", "Available");

        List<RoomDTO> rooms = roomService.getAll(filters);

        Assert.assertEquals(8, rooms.size());
        for (RoomDTO r : rooms) {
            Assert.assertEquals(RoomStatus.Available, rooms.get(0).getRoomStatus());
        }
    }

    @Test
    public void testGetAll_TypeFilter_ReturnsSingleRoom()  {
        Map<String, String> filters = new HashMap<>();
        filters.put("room_type", "Standard");

        List<RoomDTO> rooms = roomService.getAll(filters);

        Assert.assertEquals(12, rooms.size());
    //    Assert.assertEquals(RoomTypes.Standard, rooms.get(0).getRoomType());
    }

    @Test
    public void testGetAll_MultipleFilters()  {
        Map<String, String> filters = new HashMap<>();
        filters.put("room_type", "Standard");
        filters.put("status", "Available");

        List<RoomDTO> rooms = roomService.getAll(filters);

        Assert.assertEquals(8, rooms.size());
       // Assert.assertEquals(RoomTypes.Standard, rooms.get(0).getRoomType().getRoomTypeName());
        Assert.assertEquals(RoomStatus.Available, rooms.get(0).getRoomStatus());
    }

    @Test
    public void testGetAll_NoMatch_ReturnsEmptyList()  {
        Map<String, String> filters = new HashMap<>();
        filters.put("room_type", "Suite");
        filters.put("status", "Unavailable");

        List<RoomDTO> rooms = roomService.getAll(filters);

        Assert.assertTrue(rooms.isEmpty());
    }

    @Test
    public void test_getAllRooms()  {
        List<RoomDTO> rooms = roomService.getAll(new HashMap<>());
        Assert.assertEquals(12, rooms.size());
    }
}
