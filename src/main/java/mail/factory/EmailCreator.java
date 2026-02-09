package mail.factory;

import mail.EmailBase;

// this is the creator class
// its an abstract factory
public abstract class EmailCreator {
    public abstract EmailBase createEmail();
}
