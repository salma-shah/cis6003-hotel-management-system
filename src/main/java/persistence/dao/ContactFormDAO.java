package persistence.dao;

import entity.ContactForm;

import java.sql.SQLException;

public interface ContactFormDAO {
    boolean saveForm( ContactForm contactForm) throws SQLException;
}
