package dto;

public class UserCredentialDTO {
    private String username;
    private String password;

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
