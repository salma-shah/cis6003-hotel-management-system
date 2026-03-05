import business.service.IRoom;
import business.service.decorators.BreakfastDecorator;
import business.service.decorators.PoolDecorator;
import business.service.decorators.WifiDecorator;
import business.service.impl.BasicRoom;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RoomDecoratorTest {

    @Test
    public void test_roomWithPoolDecorator(){
        IRoom room =  new PoolDecorator(new BasicRoom( 20000.00));
        assertEquals(35000.00, room.getCost(), 0.001);

        String expectedEndOfStatement = "immersion.";
        assertTrue(room.getDescription().contains(expectedEndOfStatement));
        }

    @Test
    public void test_roomWithWifiAndBreakfastDecorator(){
        IRoom room =  new WifiDecorator(
                new BreakfastDecorator(
                        new BasicRoom( 20000.00)));
        assertEquals(28000.00, room.getCost(), 0.001);

        List<String> expectedKeyWords = Arrays.asList("Wifi", "Breakfast");
        assertTrue(room.getDescription().contains(expectedKeyWords.get(0)));

    }

}
