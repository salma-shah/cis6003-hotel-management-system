package business.service.impl;

import business.service.BillService;
import business.service.PaymentService;
import dto.PaymentDTO;
import entity.Payment;
import mapper.PaymentMapper;
import persistence.dao.PaymentDAO;
import persistence.dao.impl.PaymentDAOImpl;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentDAO paymentDAO;
    private final BillService billService;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAOImpl();
        this.billService = new BillServiceImpl();
    }

    @Override
    public boolean processPayment(int billId, double amount, String paymentMethod) throws SQLException {
        PaymentDTO paymentDTO = new PaymentDTO(0, billId, amount, LocalDateTime.now(), paymentMethod);

        if (paymentDAO.searchById(paymentDTO.getId()) != null) return false;

        Payment payment = PaymentMapper.toPayment(paymentDTO);
        return paymentDAO.insertPayment(payment);
    }
}
