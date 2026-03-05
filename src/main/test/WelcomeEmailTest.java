import mail.EmailUtility;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WelcomeEmailTest {
    private EmailUtility emailUtility;

    @Test
    public void testWelcomeEmail() {
        String title = "Test Email";
        String receiver = "test@gmail.com";
        String sender = "invalid@gmail.com";
        String subject = "Welcome Email";
        String body = "This is a test email";
        String password = "wrong password";

        // will attempt to send an email with skeletal send email code
       // String result = EmailUtility.sendMail(title, receiver, sender, subject, body, password);
     //   assertTrue(result.startsWith("Email couldn't be sent because:"));
    }
}
