package repo;

import entity.User;
import repo.helper.CRUDRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public interface UserRepository extends CRUDRepository<User> {

    // these methods are for checking EXISTING results
    boolean existByUsername(Connection conn, Object... args) throws SQLException;
    boolean existByEmail(Connection conn, Object... args) throws SQLException;
    User findByUsername(Connection conn, Object... args) throws SQLException;

}
