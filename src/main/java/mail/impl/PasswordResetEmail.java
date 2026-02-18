package mail.impl;

import mail.EmailBase;

public class PasswordResetEmail implements EmailBase {
    private final String name, email;

    public PasswordResetEmail(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String getReceiver() {
        return email;
    }

    @Override
    public String getSubject() {
        return "Reset your Ocean View Resort Password";
    }

    @Override
    public String getBody() {
        return "Dear " + name + "," + "\n\n" +
                "This is to inform you that the password to your Ocean View Hotel account was just changed.\n" +
                "If you did not make this change, please immediately inform HR.\n\n" +
                "Regards," + "\n" +
                "Ocean View Hotel Team.";
    }
}
