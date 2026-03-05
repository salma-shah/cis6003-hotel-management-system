import business.service.GuestService;
import business.service.impl.GuestServiceImpl;
import dto.GuestDTO;
import org.junit.Test;
import persistence.dao.GuestDAO;
import persistence.dao.impl.GuestDAOImpl;
import security.GuestRegNumGenerator;


import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GuestRegistrationTest {
    private GuestDAO guestDAO;
    private GuestRegNumGenerator guestRegNumGenerator;

    // this one should reg a guest successfully
    @Test
    public void test_successfulGuestRegistration() {
        GuestDAO guestDAO = new GuestDAOImpl();
        GuestRegNumGenerator guestRegNumGenerator = new GuestRegNumGenerator();
        GuestService guestService = new GuestServiceImpl();
        GuestDTO guestDTO = new GuestDTO.GuestDTOBuilder().firstName("John").lastName("Doe")
                .contactNumber("+94 123456789").nic("200090000821").email("johndoe@gmail.com").registrationNumber(guestRegNumGenerator.generateId())
                .build();

        boolean result = guestService.add(guestDTO);
        assertTrue(result);

    }

//    @Test
//    public void test_failedGuestRegistrationForDuplicateEmail() throws SQLException {
//        GuestDAO guestDAO = new GuestDAOImpl();
//        boolean result = guestDAO.findByEmail("johndoe@gmail.com");
//        if (result) {
//            throw new RuntimeException();
//        }
//    }

    @Test
    public void test_searchByUniqueRegNumber() {
        GuestServiceImpl guestService = new GuestServiceImpl();
        guestService.validateRegistrationNumber("OVHRC5TBANIP");

    }

    @Test
    public void test_updateGuestPassportNumber() {
        GuestDAO guestDAO = new GuestDAOImpl();
        GuestServiceImpl guestService = new GuestServiceImpl();
        GuestDTO guest = new GuestDTO.GuestDTOBuilder().passportNumber("N1122334455").build();
        boolean result = guestService.update(guest);
        if (result)
        {
            assertEquals("N1122334455", guest.getPassportNumber());
        }
    }

    @Test
    public void test_getAllGuests() {
        GuestDAO guestDAO = new GuestDAOImpl();
        GuestServiceImpl guestService = new GuestServiceImpl();
        List<GuestDTO> guests = guestService.getAll(null);

    }
}
