package mail.factory.impl;

import mail.EmailBase;
import mail.factory.EmailFactory;
import mail.impl.PasswordResetEmail;

public class PasswordResetEmailFactory extends EmailFactory {
    private final String name, email;

    public PasswordResetEmailFactory(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public EmailBase createEmail() {
        return new PasswordResetEmail(name,email);
    }
}
