package repo.helper;

import entity.SuperEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// this repo has the base methods which can be implemented by any REPO
// it promotes modularity and reusability
public interface CRUDRepository <T extends SuperEntity> {

    boolean add(Connection conn, T entity) throws SQLException;
    boolean update(Connection conn, T entity) throws SQLException;
    boolean delete(Connection conn, T entity) throws SQLException;
    T searchById(Connection conn, T entity) throws SQLException;
    List<T> getAll(Connection conn, T entity) throws SQLException;
    boolean existsByPk(Connection conn, T entity) throws SQLException;
}
