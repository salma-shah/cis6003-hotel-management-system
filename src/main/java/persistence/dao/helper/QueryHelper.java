package persistence.dao.helper;

import exception.db.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class QueryHelper {

    private QueryHelper() {}

    // For INSERT / UPDATE / DELETE
    public static boolean executeUpdate(Connection conn, String sql, Object... args) {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, args);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new DataAccessException("Database update failed: " + sql, ex);
        }
    }

    // For SELECT queries
    public static <T> T executeQuery(Connection conn,
                                     String sql,
                                     ResultSetExtractor<T> extractor,
                                     Object... args) {

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, args);

            try (ResultSet rs = ps.executeQuery()) {
                return extractor.extract(rs);
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Database query failed: " + sql, ex);
        }
    }

    private static void setParameters(PreparedStatement ps, Object... args)
            throws SQLException {

        for (int i = 0; i < args.length; i++) {
            ps.setObject(i + 1, args[i]);
        }
    }
}