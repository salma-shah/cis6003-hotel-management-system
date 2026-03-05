package mail.factory;

import mail.EmailBase;

// this is the creator class
// its an abstract factory
public abstract class EmailFactory {
    public abstract EmailBase createEmail();
}
