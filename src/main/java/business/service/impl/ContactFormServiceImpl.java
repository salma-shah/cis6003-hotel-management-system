package business.service.impl;

import business.service.ContactFormService;
import dto.ContactFormDTO;
import entity.ContactForm;
import exception.user.InvalidUserCredentialsException;
import mapper.ContactFormMapper;
import persistence.dao.ContactFormDAO;
import persistence.dao.impl.ContactFormDAOImpl;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContactFormServiceImpl implements ContactFormService {

    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(ContactFormServiceImpl.class.getName());
    private final ContactFormDAO contactFormDAO;

    public ContactFormServiceImpl() {
        this.contactFormDAO = new ContactFormDAOImpl();
    }

    @Override
    public boolean saveForm(ContactFormDTO contactFormDTO) {

        if (contactFormDTO.getUserId() == 0) {
            throw new InvalidUserCredentialsException("Invalid user ID.");
        }

        ContactForm contactForm = ContactFormMapper.toContactForm(contactFormDTO);
        return contactFormDAO.saveForm(contactForm);

    }
}
