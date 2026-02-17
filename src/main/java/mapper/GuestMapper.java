package mapper;

import dto.GuestDTO;
import entity.Guest;

import java.util.List;
import java.util.stream.Collectors;

public class GuestMapper {

    // to dto
    public static GuestDTO toGuestDTO(Guest guest) {
        if  (guest == null) {
            return null;
        }

        return new GuestDTO.GuestDTOBuilder().id(guest.getId()).registrationNumber(guest.getRegistrationNumber())
                        .firstName(guest.getFirstName()).lastName(guest.getLastName()).email(guest.getEmail())
                        .contactNumber(guest.getContactNumber()).address(guest.getAddress())
                        .nic(guest.getNic()).passportNumber(guest.getPassportNumber())
                        .dob(guest.getDob()).build();

    }

    // to entity
    public static Guest toGuest(GuestDTO guestDTO) {
        if  (guestDTO == null) {
            return null;
        }

        return new Guest.GuestBuilder().id(guestDTO.getId()).registrationNumber(guestDTO.getRegistrationNumber())
                .firstName(guestDTO.getFirstName()).lastName(guestDTO.getLastName()).email(guestDTO.getEmail())
                .contactNumber(guestDTO.getContactNumber()).address(guestDTO.getAddress()).nic(guestDTO.getNic())
                .passportNumber(guestDTO.getPassportNumber()).dob(guestDTO.getDob()).build();
    }

    // updated guest fields only
    public static Guest toUpdatedGuest(GuestDTO guestDTO) {

        return new Guest.GuestBuilder()
                .passportNumber(guestDTO.getPassportNumber())
                .contactNumber(guestDTO.getContactNumber())
                .firstName(guestDTO.getFirstName())
                .lastName(guestDTO.getLastName())
                .address(guestDTO.getAddress())
                .build();
    }

    // list
    public static List<GuestDTO> toGuestDTOList(List<Guest> guests) {
        if (guests == null) {
            return null;
        }
        return guests.stream().map(GuestMapper::toGuestDTO).collect(Collectors.toList());
    }
}

