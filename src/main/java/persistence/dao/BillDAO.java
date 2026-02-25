package persistence.dao;

import entity.Bill;

import java.sql.SQLException;

public interface BillDAO {
    int generateBill(Bill entity);
    Bill searchById(int id);
}
