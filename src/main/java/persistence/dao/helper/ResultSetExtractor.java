package persistence.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetExtractor<T> {

        T extract(ResultSet rs) throws SQLException;

}
