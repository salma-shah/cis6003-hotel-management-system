package mail;

// this is the common email interface
public interface EmailBase {
    String getReceiver();
    String getSubject();
    String getBody();
}
