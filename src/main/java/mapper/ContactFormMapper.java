package mapper;

import dto.ContactFormDTO;
import entity.ContactForm;

public class ContactFormMapper {

    // convering to dto
    public static ContactFormDTO toContactFormDTO(ContactForm contactForm) {
        if (contactForm == null) {
            return null;
        }

        return new ContactFormDTO(contactForm.getId(), contactForm.getUserId(), contactForm.getMessage());
    }

    // converting to entity
    public static ContactForm toContactForm(ContactFormDTO contactFormDTO) {
        if (contactFormDTO == null) {
            return null;
        }

        return new ContactForm(contactFormDTO.getId(), contactFormDTO.getUserId(), contactFormDTO.getMessage());
    }
}

