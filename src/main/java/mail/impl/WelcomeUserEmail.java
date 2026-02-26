package mail.impl;

import mail.EmailBase;

public class WelcomeUserEmail implements EmailBase {

    private final String name, email;
    private final String username;

    public WelcomeUserEmail(String name, String email, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
    }

    @Override
    public String getReceiver() {
        return email;
    }

    @Override
    public String getSubject() {
        return "Welcome to Ocean View Hotel";
    }

    @Override
    public String getBody() {

        return "Dear " + name + "," + "\n\n" +
                "Welcome to Ocean View Hotel. We would like to inform you that your account has been created.\n\n" +
                "Here is your username for login: \n\n" +
                "Username: " + username + "\n" +
                "Please contact the manager for information about your password. Afterwards, log into your account and change your password to secure your account.\n\n" +
                "We are happy to welcome you to our team. Please contact us if you have any questions or submit a query form here: http://localhost:8080/ocean_view_hotel_war_exploded/help" + "\n\n" +
                "Regards," + "\n" +
                "Ocean View Resort Team\n" +
                "Galle\n" +
                "Email: ocean.view.hotel.cis6003@gmail.com\n" +
                "Phone: +94 11 22 33 567";
    }
}
