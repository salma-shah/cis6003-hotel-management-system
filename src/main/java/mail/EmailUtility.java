package mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
import java.util.Properties;

public class EmailUtility {

    // method to send the email
    public static String sendMail(String receiver, String subject, String body) {
        String status = null;
        try
        {
            final String sender = "ocean.view.hotel.cis6003@gmail.com";
            final String password = "tohpobcamqemwzox ";

            // configuring stmp properties
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            // creating a session with authenticator for username and password
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender, password);
                }
            };
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);

            // wrapping a message in the session
            MimeMessage message = new MimeMessage(session);
            message.setSubject(subject);

            // setting email address of the sender and receiver
            // TO and FROM
            message.setFrom(new InternetAddress(sender));
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(receiver));
            message.setText(body);

            // sending the email through transport
            Transport.send(message);
            return status = "Email was sent successfully.";
        }
         catch (Exception ex)
         {
             return status = "Email couldn't be sent because: " + ex.getMessage();
         }
    }
}
