package entity;

import java.time.LocalDateTime;

public class Payment implements SuperEntity {
    private final int id;
    private final int billId;
    private final double amount;
    private final LocalDateTime paymentDate;
    private final String paymentMethod;

    public Payment(int id, int billId, double amount, LocalDateTime paymentDate, String paymentMethod) {
        this.id = id;
        this.billId = billId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    public int getId() {
        return id;
    }

    public int getBillId() {
        return billId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
