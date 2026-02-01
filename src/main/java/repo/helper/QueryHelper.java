package repo.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryHelper {
    public static <T> T execute (Connection conn, String sql, Object... args) throws SQLException {

        // this is creating an instance of Prepared statement
        PreparedStatement pt = conn.prepareStatement(sql);

        // this is to iterate the numbers to set the index in parameters
        for (int i = 0; i < args.length; i++) {
            pt.setObject(i + 1, args[i]);
        }

        // if the statement starts with SELECT, execute the statement
        if (sql.toLowerCase().startsWith("select") || sql.toLowerCase().startsWith("SELECT")) {
            return (T) pt.executeQuery();
        }
        return (T) ((Boolean) (pt.executeUpdate() > 0));  // converting boolean which is primitive
                                                          // to Boolean

    }
}
