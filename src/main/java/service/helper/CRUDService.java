package service.helper;

import dto.SuperDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CRUDService <T extends SuperDTO> {
        // add method is commented out because ADD functions will not require the same arguments
        // for every entity
        // boolean add(T entity) throws SQLException;
        boolean update(T entity) throws SQLException;
        boolean delete(int id) throws SQLException;
        T searchById(String id) throws SQLException;
        List<T> getAll(Map<String, String> searchParams) throws SQLException;
        boolean existsByPrimaryKey(int primaryKey) throws SQLException;
    }
