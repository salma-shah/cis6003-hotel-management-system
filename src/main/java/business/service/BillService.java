package business.service;

import dto.BillDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface BillService {
    int generateBill(String resCode, int guestId, double stayCost, double tax, double discount) ;
    byte[] generateBillPDF(int billId,String resCode,   int guestId, double stayCost, double tax, double discount, double totalAmount) throws IOException;
    double calculateTotalAmount(double stayCost, double tax, double discount) ;
    BillDTO searchById(int id) ;

}
