package mail.factory.impl;

import constant.Role;
import mail.EmailBase;
import mail.factory.EmailCreator;
import mail.impl.WelcomeUserEmail;

public class WelcomeEmailCreator extends EmailCreator {

    private final String name, email;
    private final String username;
    private final String password;

    public WelcomeEmailCreator(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    @Override
    public EmailBase createEmail() {
        return new WelcomeUserEmail(name,  email, username, password);
    }
}
