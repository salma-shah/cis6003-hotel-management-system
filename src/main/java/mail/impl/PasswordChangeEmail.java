package mail.impl;

import mail.EmailBase;

public class PasswordChangeEmail implements EmailBase {
    private final String name, email;

    public PasswordChangeEmail(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String getReceiver() {
        return email;
    }

    @Override
    public String getSubject() {
        return "Your Ocean View Resort account password was changed.";
    }

    @Override
    public String getBody() {
        return "Dear " + name + "," + "\n\n" +
                "This is to inform you that the password to your Ocean View Hotel account was just changed.\n" +
                "If you did not make this change, please immediately inform HR.\n\n" +
                "Regards," + "\n" +
                "Ocean View Resort Team\n" +
                "Galle\n" +
                "Email: ocean.view.hotel.cis6003@gmail.com\n" +
                "Phone: +94 11 22 33 567";
    }
}
