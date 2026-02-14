package business.service;

import dto.ContactFormDTO;
import java.sql.SQLException;

public interface ContactFormService {
    boolean saveForm(ContactFormDTO contactForm) throws SQLException;
}
