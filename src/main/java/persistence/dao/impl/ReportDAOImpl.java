package persistence.dao.impl;

import db.DBConnection;
import exception.db.DataAccessException;
import persistence.dao.ReportDAO;
import persistence.dao.helper.QueryHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public int getTotalReservations(LocalDate startDate, LocalDate endDate) {
        try(Connection connection = DBConnection.getInstance().getConnection()) {
            return QueryHelper.executeQuery(connection, "SELECT COUNT(*) FROM reservation WHERE date_of_res BETWEEN ? AND ?",
                    rs ->
                    {
                        if(rs.next()) {
                            return rs.getInt(1);
                        }
                        return 0;  // if no count
                    }, startDate, endDate);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error getting total reservations");
        }
    }

    @Override
    public Map<String, Integer> getReservationsByStatus(LocalDate startDate, LocalDate endDate) {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT status, COUNT(*) FROM reservation WHERE date_of_res BETWEEN ? AND ? " +
                            "GROUP BY status",
                    rs ->
                    {
                        Map<String, Integer> map = new HashMap<>();
                        while(rs.next()) {
                            String status = rs.getString(1);
                            int count = rs.getInt(2);
                            map.put(status, count);
                        }
                        return map;
                    },
                    startDate, endDate);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error getting reservation status breakdown");
        }
    }

    @Override
    public double getTotalStayRevenue(LocalDate startDate, LocalDate endDate) {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT SUM(total_cost) FROM reservation WHERE date_of_res BETWEEN ? AND ?",
                    rs ->
                    {
                        if(rs.next()) {
                            return rs.getDouble(1);
                        }
                        return 0.0;
                    },
                    startDate, endDate);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error getting total stay revenue");
        }
    }

    @Override
    public int getNewGuests(LocalDate startDate, LocalDate endDate) {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT COUNT(*) FROM guest WHERE created_at BETWEEN ? AND ?",
                    rs ->
                    {
                        if(rs.next()) {
                            return rs.getInt(1);
                        }
                        return 0;
                    },
                    startDate, endDate);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error getting number of new guests");
        }
    }

    @Override
    public int getReturnedGuests(LocalDate startDate, LocalDate endDate) {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT COUNT(*) FROM guest WHERE created_at NOT BETWEEN ? AND ?",
                    rs ->
                    {
                        if(rs.next()) {
                            return rs.getInt(1);
                        }
                        return 0;
                    },
                    startDate, endDate);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error getting number of returning guests");
        }
    }

    @Override
    public double getTotalTax(LocalDate startDate, LocalDate endDate) {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT SUM(tax) FROM bill WHERE created_at BETWEEN ? AND ?",
                    rs ->
                    {
                        if(rs.next()) {
                            return rs.getDouble(1);
                        }
                        return 0.0;
                    },
                    startDate, endDate);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error getting total tax");
        }
    }

    @Override
    public double getTotalNetRevenue(LocalDate startDate, LocalDate endDate) {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT SUM(total_amount) FROM bill WHERE created_at BETWEEN ? AND ?",
                    rs ->
                    {
                        if(rs.next()) {
                            return rs.getDouble(1);
                        }
                        return 0.0;
                    },
                    startDate, endDate);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error getting total net revenue");
        }
    }

    @Override
    public Map<String, Integer> getPaymentStatusBreakdown(LocalDate startDate, LocalDate endDate) {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT status, COUNT(*) FROM bill WHERE created_at BETWEEN ? AND ? " +
                            "GROUP BY status",
                    rs ->
                    {
                        Map<String, Integer> map = new HashMap<>();
                        while(rs.next()) {
                            String status = rs.getString(1);
                            int count = rs.getInt(2);
                            map.put(status, count);
                        }
                        return map;
                    },
                    startDate, endDate);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error getting payment status breakdown");
        }
    }

    @Override
    public double getPaidAmounts(LocalDate startDate, LocalDate endDate) {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT SUM(total_amount) FROM bill WHERE status = 'Paid' AND created_at BETWEEN ? AND ?",
                    rs ->
                    {
                        if(rs.next()) {
                            return rs.getDouble(1);
                        }
                        return 0.0;
                    },
                    startDate, endDate);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error getting paid revenue");
        }
    }

    @Override
    public double getOutstandingRevenue(LocalDate startDate, LocalDate endDate) {
        try(Connection connection = DBConnection.getInstance().getConnection())
        {
            return QueryHelper.executeQuery(connection, "SELECT SUM(total_amount) FROM bill WHERE status = 'Pending' AND created_at BETWEEN ? AND ? ",
                    rs ->
                    {
                        if(rs.next()) {
                            return rs.getDouble(1);
                        }
                        return 0.0;
                    },
                    startDate, endDate);
        }
        catch (SQLException ex)
        {
            throw new DataAccessException("Error getting outstanding revenue");
        }
    }
}
