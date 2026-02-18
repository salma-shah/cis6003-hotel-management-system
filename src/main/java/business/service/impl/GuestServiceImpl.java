package business.service.impl;

import business.service.GuestService;
import dto.GuestDTO;
import dto.RoomDTO;
import entity.Guest;
import mapper.GuestMapper;
import mapper.RoomMapper;
import persistence.dao.GuestDAO;
import persistence.dao.impl.GuestDAOImpl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuestServiceImpl implements GuestService {
    private static final Logger LOG = Logger.getLogger(GuestServiceImpl.class.getName());
    private final GuestDAO guestDAO;

    public GuestServiceImpl() {
        this.guestDAO = new GuestDAOImpl();
    }

    @Override
    public boolean update(GuestDTO guestDTO) throws SQLException {
        try {
            return guestDAO.update(GuestMapper.toUpdatedGuest(guestDTO));
        }
        catch (SQLException ex) {
            LOG.log(Level.INFO, "Something went wrong with updating the guest");
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try  {
            // if the guest for the specific PK exists, delete the user
            if (guestDAO.existsByPrimaryKey( id))
            {
                return guestDAO.delete( id);
            }}
        catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
        return false;
    }

    @Override
    public GuestDTO searchById(int id) throws SQLException {
        return GuestMapper.toGuestDTO(guestDAO.searchById(id));
    }

    @Override
    public List<GuestDTO> getAll(Map<String, String> searchParams) throws SQLException {
        try {
            Map<String, String> filters = (searchParams!= null) ? new HashMap<>(searchParams) : new HashMap<>();

            return GuestMapper.toGuestDTOList(guestDAO.getAll(filters));
        }
        catch (SQLException e)
        {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public boolean existsByPrimaryKey(int primaryKey) throws SQLException {
        try
        {
            return guestDAO.existsByPrimaryKey(primaryKey);
        }
        catch (Exception ex)
        {
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean add(GuestDTO guestDTO) throws SQLException {
        try {
            if (guestDAO.findByEmail(guestDTO.getEmail())) {
                return false; // email already exists
            }
            Guest guest = GuestMapper.toGuest(guestDTO);
            LOG.log(Level.INFO, "Guest has been registered successfully:" + guest);
            return guestDAO.add(guest);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error registering guest in service layer", ex);
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public boolean findByRegistrationNumber(String registrationNumber) throws SQLException {
        try {
            return guestDAO.findByRegistrationNumber(registrationNumber);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error finding guest in service layer");
            throw new SQLException(ex.getMessage());
        }
    }
}
