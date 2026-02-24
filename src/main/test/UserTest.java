import business.service.UserService;
import business.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class UserTest {
    @Test
    public void test_changePassword() throws Exception {
        UserService userService = new UserServiceImpl();
        boolean changedPw = userService.changePassword("Salma-Shah", "salmashah1234!!");
        Assert.assertTrue(changedPw);
    }
}
