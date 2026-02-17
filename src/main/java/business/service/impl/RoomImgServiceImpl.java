package business.service.impl;

import business.service.RoomImgService;
import dto.RoomImgDTO;
import entity.RoomImg;
import mapper.RoomImgMapper;
import persistence.dao.RoomImgDAO;
import persistence.dao.impl.RoomImgDAOImpl;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomImgServiceImpl implements RoomImgService {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(RoomImgServiceImpl.class.getName());
    // private final Connection connection = DBConnection.getConnection();
    private final RoomImgDAO roomImgDAO;

    public  RoomImgServiceImpl() {
        this.roomImgDAO = new RoomImgDAOImpl();
    }
    @Override
    public boolean saveImg(RoomImgDTO roomImgDTO) throws SQLException {
        try{
            RoomImg roomImgEntity = RoomImgMapper.toRoomImg(roomImgDTO);
            LOG.log(Level.INFO, "The room image : " + roomImgEntity + " was successfully added.");
            return roomImgDAO.saveImg(roomImgDTO.getRoomId(), roomImgDTO.getImgPath(), roomImgDTO.getAlt());
        }
        catch(SQLException ex){
            LOG.severe("Error adding room: " + ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }
}
