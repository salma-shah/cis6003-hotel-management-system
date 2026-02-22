package persistence.dao;

import entity.Bill;

import java.sql.SQLException;

public interface BillDAO {
    int generateBill(Bill entity) throws SQLException;
    Bill searchById(int id) throws SQLException;
    int getLastInsertedId() throws SQLException;
}
