package persistence.dao.helper;

import entity.SuperEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

// this repo has the base methods which can be implemented by any REPO
// it promotes modularity and reusability
public interface CRUDDAO<T extends SuperEntity> {

    boolean add(T entity) ;
    boolean update(T entity) ;
    boolean delete(int id) ;
    T searchById(int id) ;
    List<T> getAll(Map<String, String> searchParams) ;
    boolean existsByPrimaryKey(int primaryKey) ;

}
