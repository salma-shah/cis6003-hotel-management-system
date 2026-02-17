package persistence.dao.helper;

import entity.SuperEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

// this repo has the base methods which can be implemented by any REPO
// it promotes modularity and reusability
public interface CRUDDAO<T extends SuperEntity> {

    boolean add(T entity) throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(int id) throws SQLException;
    T searchById(int id) throws SQLException;
    List<T> getAll(Map<String, String> searchParams) throws SQLException;
    boolean existsByPrimaryKey(int primaryKey) throws SQLException;

}
