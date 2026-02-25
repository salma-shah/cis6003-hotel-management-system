package business.service.impl;

import business.service.RoomImgService;
import dto.RoomImgDTO;
import persistence.dao.RoomImgDAO;
import persistence.dao.impl.RoomImgDAOImpl;

public class RoomImgServiceImpl implements RoomImgService {
    private final RoomImgDAO roomImgDAO;

    public  RoomImgServiceImpl() {
        this.roomImgDAO = new RoomImgDAOImpl();
    }
    @Override
    public boolean saveImg(RoomImgDTO roomImgDTO) {
        return roomImgDAO.saveImg(roomImgDTO.getRoomId(), roomImgDTO.getImgPath(), roomImgDTO.getAlt());

    }
}
