package persistence.dao.impl;

import db.DBConnection;
import entity.ContactForm;
import exception.db.DataAccessException;
import persistence.dao.ContactFormDAO;
import persistence.dao.helper.QueryHelper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ContactFormDAOImpl implements ContactFormDAO {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(ContactFormDAOImpl.class.getName());

    @Override
    public boolean saveForm(ContactForm contactForm) {
        try(Connection conn = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeUpdate(conn, "INSERT INTO contact_form (user_id, message) VALUES (?, ?)", contactForm.getUserId(), contactForm.getMessage());
        }
        catch(SQLException ex)
        {
            throw new DataAccessException("There was an error inserting the form", ex);
        }
    }
}
