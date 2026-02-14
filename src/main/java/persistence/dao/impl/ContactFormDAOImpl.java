package persistence.dao.impl;

import entity.ContactForm;
import persistence.dao.ContactFormDAO;
import persistence.dao.helper.QueryHelper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContactFormDAOImpl implements ContactFormDAO {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(ContactFormDAOImpl.class.getName());

    @Override
    public boolean saveForm(Connection connection, ContactForm contactForm) throws SQLException {

        try
        {
            return QueryHelper.execute(connection, "INSERT INTO contact_form (user_id, message) VALUES (?, ?)", contactForm.getUserId(), contactForm.getMessage());
        }
        catch(SQLException ex)
        {
            LOG.log(Level.SEVERE, "There was an error saving the contact form: ", ex);
            throw new SQLException(ex.getMessage());
        }
    }
}
