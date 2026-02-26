package business.service.helper;

import dto.SuperDTO;

import java.util.List;
import java.util.Map;

public interface CRUDService <T extends SuperDTO> {
        // add method is commented out because ADD functions will not require the same arguments
        // for every entity
        // boolean add(T entity) ;
        boolean update(T entity) ;
        boolean delete(int id) ;
        T searchById(int id) ;
        List<T> getAll(Map<String, String> searchParams) ;
        boolean existsByPrimaryKey(int primaryKey) ;
    }
