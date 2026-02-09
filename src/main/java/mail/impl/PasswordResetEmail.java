package mail.impl;

import mail.EmailBase;

public class PasswordResetEmail implements EmailBase {
    @Override
    public String getReceiver() {
        return "";
    }

    @Override
    public String getSubject() {
        return "";
    }

    @Override
    public String getBody() {
        return "";
    }
}
