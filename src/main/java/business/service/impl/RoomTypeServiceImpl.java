package business.service.impl;

import business.service.RoomTypeService;
import dto.RoomTypeDTO;
import entity.RoomType;
import mapper.RoomTypeMapper;
import persistence.dao.RoomTypeDAO;
import persistence.dao.impl.RoomTypeDAOImpl;

import java.util.List;

public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeDAO roomTypeDAO;

    public RoomTypeServiceImpl() {
        roomTypeDAO = new RoomTypeDAOImpl();
    }

    @Override
    public RoomTypeDTO getById(int id)  {

        if (id <=0 ) { throw new IllegalArgumentException("RoomType ID is invalid");}
        return RoomTypeMapper.toRoomTypeDTO(roomTypeDAO.searchById(id));
    }

    @Override
    public RoomTypeDTO getByRoomId(int id)  {
        if (id <=0 ) { throw new IllegalArgumentException("Room ID is invalid");}
        RoomType roomType = roomTypeDAO.findByRoomId(id);
        return RoomTypeMapper.toRoomTypeDTO(roomType);
    }

    @Override
    public List<RoomTypeDTO> getAll()  {
        return RoomTypeMapper.roomTypeDTOList(roomTypeDAO.getAll(null));
    }

}
