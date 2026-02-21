package business.service.impl;

import business.service.RoomTypeService;
import dto.RoomTypeDTO;
import mapper.RoomTypeMapper;
import persistence.dao.RoomTypeDAO;
import persistence.dao.impl.RoomTypeDAOImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeDAO roomTypeDAO;

    public RoomTypeServiceImpl() {
        roomTypeDAO = new RoomTypeDAOImpl();
    }

    @Override
    public RoomTypeDTO getById(int id) throws SQLException {
      return RoomTypeMapper.toRoomTypeDTO(roomTypeDAO.searchById(id));
    }

    @Override
    public RoomTypeDTO getByRoomId(int id) throws SQLException {
        return RoomTypeMapper.toRoomTypeDTO(roomTypeDAO.findByRoomId(id));
    }

    @Override
    public List<RoomTypeDTO> getAll() throws SQLException {
        return RoomTypeMapper.roomTypeDTOList(roomTypeDAO.getAll(null));
    }

//    private RoomTypeDTO mapToDTO(RoomType entity) {
//        return new RoomTypeDTO.Builder().roomTypeId(entity.getRoomTypeId()).roomTypeName(entity.getRoomTypeName())
//                        .basePricePerNight(entity.getBasePricePerNight()).build();
//    }
}
