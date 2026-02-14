package business.service.impl;

import business.service.ContactFormService;
import db.DBConnection;
import dto.ContactFormDTO;
import entity.ContactForm;
import mapper.ContactFormMapper;
import persistence.dao.ContactFormDAO;
import persistence.dao.impl.ContactFormDAOImpl;

import java.sql.Connection;
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
    public boolean saveForm(ContactFormDTO contactFormDTO) throws SQLException {
        try(Connection conn = DBConnection.getInstance().getConnection()) {
            ContactForm contactForm = ContactFormMapper.toContactForm(contactFormDTO);
            return contactFormDAO.saveForm(conn, contactForm);
        }
        catch (SQLException ex) {
            LOG.log(Level.SEVERE, "There was an error saving the form in the service layer: ");
            throw new SQLException(ex.getMessage());
        }
    }
}
