package business.service;

import dto.BillDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface BillService {
    int generateBill(String resCode, String guestCode, double stayCost, double tax, double discount, double totalAmount ) throws SQLException;
    byte[] generateBillPDF(String resCode, String guestCode, double stayCost, double tax, double discount, double totalAmount) throws IOException;
    int getLastInsertedId() throws SQLException;

}
