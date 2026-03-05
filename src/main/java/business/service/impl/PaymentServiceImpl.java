package business.service.impl;

import business.service.BillService;
import business.service.PaymentService;
import dto.PaymentDTO;
import entity.Payment;
import exception.bill.BillNotFoundException;
import mapper.PaymentMapper;
import persistence.dao.PaymentDAO;
import persistence.dao.impl.PaymentDAOImpl;

import java.time.LocalDateTime;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentDAO paymentDAO;
    private final BillService billService;
    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAOImpl();
        this.billService = new BillServiceImpl();
    }

    @Override
    public boolean processPayment(int billId, double amount, String paymentMethod){

        if (billId == 0){
            throw new IllegalArgumentException("Bill Id can't be empty");
        }

        if (billService.searchById(billId) == null){
            throw new BillNotFoundException("Bill with ID " + billId + " not found");
        }

        if (amount <= 0){
            throw new IllegalArgumentException("Total amount can't be negative");
        }

        PaymentDTO paymentDTO = new PaymentDTO(0, billId, amount, LocalDateTime.now(), paymentMethod);

        Payment payment = PaymentMapper.toPayment(paymentDTO);
        return paymentDAO.insertPayment(payment);
    }

}
