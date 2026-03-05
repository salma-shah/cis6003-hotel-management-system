package dto;

public class UserCredentialDTO implements SuperDTO{
    private final String  username;
    private final String password;

    public UserCredentialDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
