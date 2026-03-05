package mapper;

import dto.AmenityDTO;
import dto.ContactFormDTO;
import entity.Amenity;
import entity.ContactForm;

public class AmenityMapper {
    public static Amenity toAmenity(AmenityDTO amenityDTO) {
        if (amenityDTO == null) {
            return null;
        }

        return new Amenity(amenityDTO.getId(), amenityDTO.getName(), amenityDTO.getCost());
    }

    public static AmenityDTO toAmenityDTO(Amenity amenity) {
        if  (amenity == null) {
            return null;
        }

        return new AmenityDTO(amenity.getId(), amenity.getName(), amenity.getCost());
    }
}
