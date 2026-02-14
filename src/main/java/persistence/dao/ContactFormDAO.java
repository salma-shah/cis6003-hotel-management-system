package persistence.dao;

import entity.ContactForm;

import java.sql.Connection;
import java.sql.SQLException;

public interface ContactFormDAO {
    boolean saveForm(Connection connection, ContactForm contactForm) throws SQLException;
}
