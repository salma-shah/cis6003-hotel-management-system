package entity;

import constant.Role;

import java.util.Date;

public class User implements SuperEntity {
    private final String userId;
    private final String username;
    private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String contactNumber;
    private final Role role;
    private final Date createdAt;
    private final Date updatedAt;
    private final Date deletedAt;

    // getters
    // no setters because when using builder pattern, the class becomes immutable
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    // to string
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }

    // using builder pattern
    private User (UserBuilder builder)
    {
        this.userId = builder.userId;
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.contactNumber = builder.contactNumber;
        this.role = builder.role;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.deletedAt = builder.deletedAt;
    }

    // public static builder class
    public static class UserBuilder{
        private String userId;
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private String contactNumber;
        private Role role;
        private Date createdAt;
        private Date updatedAt;
        private Date deletedAt;

        public UserBuilder() {}

        public UserBuilder userId(String userId)
        {
            this.userId = userId;
            return this;
        }

        public UserBuilder username(String username)
        {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password)
        {
            this.password = password;
            return this;
        }

        public UserBuilder email(String email)
        {
            this.email = email;
            return this;
        }

        public UserBuilder firstName(String firstName)
        {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName)
        {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder contactNumber(String contactNumber)
        {
            this.contactNumber = contactNumber;
            return this;
        }

        public UserBuilder role(Role role)
        {
            this.role = role;
            return this;
        }

        public UserBuilder createdAt(Date createdAt)
        {
            this.createdAt = createdAt;
            return this;
        }

        public UserBuilder updatedAt(Date updatedAt)
        {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserBuilder deletedAt(Date deletedAt)
        {
            this.deletedAt = deletedAt;
            return this;
        }

        public User build()
        {
            return new User(this);
        }
    }
}
