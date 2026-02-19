package entity;

import java.time.LocalDateTime;
import java.util.Date;

public class Guest implements SuperEntity {
    private final int id;
    private final String registrationNumber;
    private final String firstName;
    private final String lastName;
    private final String contactNumber;
    private final String address;
    private final String email, nationality  ;
    private final String nic, passportNumber;
    private final Date dob;
    private final LocalDateTime createdAt, updatedAt;

    public int getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
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

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getNic() {
        return nic;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public Date getDob() {
        return dob;
    }

    public String getNationalty() {
        return nationality;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", nic=" + nic +
                ", passportNumber=" + passportNumber +
                ", dob=" + dob +
                ", nationality=" + nationality +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // builder pattern
    // builder constructor
    private Guest (GuestBuilder builder)
    {
        this.id = builder.id;
        this.registrationNumber = builder.registrationNumber;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.contactNumber = builder.contactNumber;
        this.address = builder.address;
        this.email = builder.email;
        this.nic = builder.nic;
        this.passportNumber = builder.passportNumber;
        this.dob = builder.dob;
        this.nationality = builder.nationality;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }
    // builder class
    public static class GuestBuilder {
        private int id;
        private String registrationNumber;
        private String firstName, lastName, contactNumber, address, email;
        private String nic;
        private String passportNumber, nationality;
        private Date dob;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public GuestBuilder(){}
        public GuestBuilder id(int id) {
            this.id = id;
            return this;
        }
        public GuestBuilder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }
        public GuestBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public GuestBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public GuestBuilder contactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }
        public GuestBuilder nationality(String nationality) {
            this.nationality = nationality;
            return this;
        }
        public GuestBuilder address(String address) {
            this.address = address;
            return this;
        }
        public GuestBuilder email(String email) {
            this.email = email;
            return this;
        }
        public GuestBuilder nic(String nic) {
            this.nic = nic;
            return this;
        }
        public GuestBuilder passportNumber(String passportNumber) {
            this.passportNumber = passportNumber;
            return this;
        }
        public GuestBuilder dob(Date dob) {
            this.dob = dob;
            return this;
        }
        public GuestBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public GuestBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public Guest build() {
            return new Guest(this);
        }
    }
}
