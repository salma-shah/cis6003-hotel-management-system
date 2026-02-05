package dto;

import constant.Role;
import entity.SuperEntity;

import java.util.Date;

// user data-to-obj is safe to expose
public class UserDTO implements SuperDTO {
    private final int userId;
    private final String username;
   // private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String contactNumber;
    private final String address;
    private final Role role;
    private final Date createdAt;
    private final Date updatedAt;
    private final Date deletedAt;

    // again builder pattern
    private UserDTO(UserDTOBuilder userDTOBuilder) {
        this.userId = userDTOBuilder.userId;
        this.username = userDTOBuilder.username;
      //  this.password = userDTOBuilder.password;
        this.email = userDTOBuilder.email;
        this.firstName = userDTOBuilder.firstName;
        this.lastName = userDTOBuilder.lastName;
        this.contactNumber = userDTOBuilder.contactNumber;
        this.role = userDTOBuilder.role;
        this.address = userDTOBuilder.address;
        this.createdAt = userDTOBuilder.createdAt;
        this.updatedAt = userDTOBuilder.updatedAt;
        this.deletedAt = userDTOBuilder.deletedAt;
    }

    // getters only again as it is immutable
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
//
//    public String getPassword() {
//        return password;
//    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Role getRole() {
        return role;
    }
    public String getAddress() {return address;}

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
            //    ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", role=" + role +
                ", address=" + address +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }

    // public static builder class
    public static class UserDTOBuilder {
        private int userId;
        private String username;
    //    private String password;
        private String email;
        private String firstName;
        private String lastName;
        private String contactNumber;
        private Role role;
        private String address;
        private Date createdAt;
        private Date updatedAt;
        private Date deletedAt;

        public UserDTOBuilder() {
        }

        // builder methods for every variable
        public UserDTO.UserDTOBuilder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public UserDTO.UserDTOBuilder username(String username) {
            this.username = username;
            return this;
        }

//        public UserDTO.UserDTOBuilder password(String password) {
//            this.password = password;
//            return this;
//        }

        public UserDTO.UserDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserDTO.UserDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserDTO.UserDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserDTO.UserDTOBuilder contactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }

        public UserDTO.UserDTOBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserDTO.UserDTOBuilder address(String address) {
            this.address = address;
            return this;
        }

        public UserDTO.UserDTOBuilder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserDTO.UserDTOBuilder updatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserDTO.UserDTOBuilder deletedAt(Date deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        // setting up method to set the times of creation, updating and deleting
        public UserDTOBuilder setTimeStamps(){
            Date now = new Date();
            this.createdAt = now;
            this.updatedAt = now;
            return this;
        }

        // this creates the instance of the DTO class
        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
