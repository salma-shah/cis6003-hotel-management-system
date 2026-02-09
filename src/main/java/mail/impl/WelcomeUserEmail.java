package mail.impl;

import constant.Role;
import mail.EmailBase;

public class WelcomeUserEmail implements EmailBase {

    private final String name, email;
    private final String username;
    private final String password;

    public WelcomeUserEmail(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
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
                "Here are your login details: \n\n" +
                "Username: " + username + "\n" +
                "Password: " + password + "\n\n" +
                "Please log into your account and change your password to secure your account.\n\n" +
                "We are happy to welcome you to our team. Please contact us if you have any questions." + "\n\n" +
                "Regards," + "\n" +
                "Ocean View Hotel Team.";
    }
}
