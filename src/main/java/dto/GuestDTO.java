package dto;


import java.time.LocalDateTime;
import java.util.Date;

public class GuestDTO implements SuperDTO
{
    private final int id;
    private final String registrationNumber;
    private final String firstName;
    private final String lastName;
    private final String contactNumber;
    private final String address;
    private final String email, nationality;
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

    public String getNationality() {
        return nationality;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "GuestDTO{" +
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
                ", nationality='" + nationality + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // builder pattern
    // builder constructor
    private GuestDTO (GuestDTO.GuestDTOBuilder builder)
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
    public static class GuestDTOBuilder {
        private int id;
        private String registrationNumber;
        private String firstName, lastName, contactNumber, address, email;
        private String nic, nationality;
        private String passportNumber;
        private Date dob;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public GuestDTOBuilder(){}
        public GuestDTO.GuestDTOBuilder id(int id) {
            this.id = id;
            return this;
        }
        public GuestDTO.GuestDTOBuilder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }
        public GuestDTO.GuestDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public GuestDTO.GuestDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public GuestDTO.GuestDTOBuilder contactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }
        public GuestDTO.GuestDTOBuilder address(String address) {
            this.address = address;
            return this;
        }
        public GuestDTO.GuestDTOBuilder email(String email) {
            this.email = email;
            return this;
        }
        public GuestDTO.GuestDTOBuilder nic(String nic) {
            this.nic = nic;
            return this;
        }
        public GuestDTO.GuestDTOBuilder passportNumber(String passportNumber) {
            this.passportNumber = passportNumber;
            return this;
        }
        public GuestDTO.GuestDTOBuilder dob(Date dob) {
            this.dob = dob;
            return this;
        }
        public GuestDTO.GuestDTOBuilder nationality(String nationality) {
            this.nationality = nationality;
            return this;
        }
        public GuestDTO.GuestDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public GuestDTO.GuestDTOBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public GuestDTO build() {
            return new GuestDTO(this);
        }
    }
}
