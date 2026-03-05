package mail.factory.impl;

import mail.EmailBase;
import mail.factory.EmailFactory;
import mail.impl.PasswordChangeEmail;

public class PasswordChangeEmailFactory extends EmailFactory {
    private final String name, email;

    public PasswordChangeEmailFactory(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public EmailBase createEmail() {
        return new PasswordChangeEmail(name,email);
    }
}
