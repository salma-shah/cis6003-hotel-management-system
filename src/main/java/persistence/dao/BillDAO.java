package persistence.dao;

import entity.Bill;

public interface BillDAO {
    int generateBill(Bill entity);
    Bill searchById(int id);
}
