package business.service.impl;

import business.service.GuestService;
import dto.GuestDTO;
import dto.GuestHistoryDTO;
import entity.Guest;
import exception.EntityNotFoundException;
import exception.guest.DuplicateGuestRegNumException;
import exception.guest.GuestNotFoundException;
import mapper.GuestMapper;
import persistence.dao.GuestDAO;
import persistence.dao.impl.GuestDAOImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuestServiceImpl implements GuestService {
    private static final Logger LOG = Logger.getLogger(GuestServiceImpl.class.getName());
    private final GuestDAO guestDAO;
    private final Set<String> emails = ConcurrentHashMap.newKeySet();
    private final Set<String> regNums = ConcurrentHashMap.newKeySet();
    private final Set<String> nics = ConcurrentHashMap.newKeySet();

    private void preloadUniqueFields() {
        List<GuestDTO> guestDTOS = getAll(null);
        // lambda function
        guestDTOS.forEach(guestDTO -> {
            nics.add(guestDTO.getNic());
            regNums.add(guestDTO.getRegistrationNumber());
            emails.add(guestDTO.getEmail());
        });
    }

    public GuestServiceImpl() {
        this.guestDAO = new GuestDAOImpl();
        preloadUniqueFields();
    }

    @Override
    public boolean update(GuestDTO guestDTO)  {
        return guestDAO.update(GuestMapper.toUpdatedGuest(guestDTO));
    }

    @Override
    public boolean delete(int id)  {
        if (id <=0 )
        {
            throw new IllegalArgumentException("Invalid guest ID");
        }

        // if the guest for the specific PK exists, delete the user
        if (!guestDAO.existsByPrimaryKey(id))
        {
            throw new GuestNotFoundException("Guest Not Found for ID: " + id);
        }
        return guestDAO.delete(id);
    }

    @Override
    public GuestDTO searchById(int id)  {
        if (id <= 0 )
        {
            throw new IllegalArgumentException("Invalid guest ID");
        }
        try {
            Guest guest = guestDAO.searchById(id);
            return GuestMapper.toGuestDTO(guest);
        } catch (EntityNotFoundException e) {
            LOG.log(Level.WARNING, "Guest Not Found for ID: " + id, e);
            throw new GuestNotFoundException("Guest for ID: " + id + " not found");
        }
    }

    @Override
    public List<GuestDTO> getAll(Map<String, String> searchParams)  {
        Map<String, String> filters = (searchParams!= null) ? new HashMap<>(searchParams) : new HashMap<>();
        return GuestMapper.toGuestDTOList(guestDAO.getAll(filters));
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey)  {

        if (primaryKey <= 0)
        {
            throw new IllegalArgumentException("Invalid guest ID");
        }

        return guestDAO.existsByPrimaryKey(primaryKey);
    }

    @Override
    public boolean add(GuestDTO guestDTO)  {
        // ensuring reg num is unique
        if(guestDAO.existsByRegistrationNumber(guestDTO.getRegistrationNumber()))
        {
            throw new DuplicateGuestRegNumException("Registration Number " + guestDTO.getRegistrationNumber() + " already exists");
        }

        findGuestByEmail(guestDTO.getEmail());
        if (guestDTO.getNic() != null)
        {
            findGuestByNic(guestDTO.getNic());
        }

        if (guestDTO.getPassportNumber() != null)
        {
            findGuestByPassport(guestDTO.getPassportNumber());
        }

        Guest guest = GuestMapper.toGuest(guestDTO);
        LOG.log(Level.INFO, "Guest has been registered successfully:" + guest);
        boolean successfulReg= guestDAO.add(guest);
        if (!successfulReg)
        {
            return false;
        }
        if (guestDTO.getNic() != null) nics.add(guestDTO.getNic());
        if (guestDTO.getEmail() != null) emails.add(guestDTO.getEmail());
        if (guestDTO.getRegistrationNumber() != null) regNums.add(guestDTO.getRegistrationNumber());
        return true;
    }

    @Override
    public Integer findGuestIdByRegistrationNumber(String registrationNumber)  {
        if  (registrationNumber == null || registrationNumber.isEmpty() ) { throw new IllegalArgumentException("Invalid guest registration number"); }

        Integer guestId = guestDAO.findGuestIdByRegistrationNumber(registrationNumber);
        if (guestId == null) {
            throw new GuestNotFoundException("Guest ID Not Found for Reg Num: " + registrationNumber);
        }
        return guestId;
    }

    @Override
    public String findGuestRegNumById(int id) {

        if  (id <=0 ) { throw new IllegalArgumentException("Invalid guest ID");}

        String guestRegNum = guestDAO.findGuestRegNumById(id);
        if (guestRegNum == null) {
            throw new GuestNotFoundException("Guest Reg Num Not Found for ID: " + id);
        }
        return guestRegNum;
    }

    @Override
    public boolean validateRegistrationNumber(String registrationNumber)  {

        if (registrationNumber == null || registrationNumber.isEmpty())
        {
            throw new IllegalArgumentException("Invalid registration number");
        }

        boolean validRegNum = guestDAO.existsByRegistrationNumber(registrationNumber);

        if (!validRegNum)
        {
            throw new GuestNotFoundException("Guest Not Found for Registration Number: " + registrationNumber);
        }
        return true;
    }

    @Override
    public GuestHistoryDTO getGuestHistoryById(int id) {
        if  (id <=0 ) { throw new IllegalArgumentException("Invalid guest ID");}
        return guestDAO.getGuestHistoryById(id);
    }

    @Override
    public boolean findGuestByEmail(String email) {
        if (email == null || email.isEmpty())
        {
            throw new IllegalArgumentException("Invalid guest email");
        }
        return guestDAO.findByField("email", email);
    }

    @Override
    public boolean findGuestByNic(String nic) {
        if (nic == null && nic.isBlank())
        {
            throw new IllegalArgumentException("Invalid guest nic");
        }
        return guestDAO.findByField("nic", nic);
    }

    @Override
    public boolean findGuestByPassport(String passport) {
       if (passport == null && passport.isBlank())
       {
           throw new IllegalArgumentException("Invalid guest passport");
       }
       return guestDAO.findByField("passport_number", passport);
    }
}
