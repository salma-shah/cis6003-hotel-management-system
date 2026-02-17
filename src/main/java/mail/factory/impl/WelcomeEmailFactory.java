package mail.factory.impl;

import mail.EmailBase;
import mail.factory.EmailFactory;
import mail.impl.WelcomeUserEmail;

public class WelcomeEmailFactory extends EmailFactory {

    private final String name, email;
    private final String username;
    private final String password;

    public WelcomeEmailFactory(String name, String email, String username, String password) {
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
