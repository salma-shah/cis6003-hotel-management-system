package persistence.dao.helper;

import entity.SuperEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

// this repo has the base methods which can be implemented by any REPO
// it promotes modularity and reusability
public interface CRUDDAO<T extends SuperEntity> {

    boolean add(Connection conn, T entity) throws SQLException;
    boolean update(Connection conn, T entity) throws SQLException;
    boolean delete(Connection conn, int id) throws SQLException;
    T searchById(Connection conn, int id) throws SQLException;
    List<T> getAll(Connection conn, Map<String, String> searchParams) throws SQLException;
    boolean existsByPrimaryKey(Connection conn, int primaryKey) throws SQLException;

}
