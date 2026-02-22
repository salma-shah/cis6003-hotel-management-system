package dto;

import constant.PaymentStatus;

import java.time.LocalDateTime;

public class BillDTO implements SuperDTO {
    private final int id;
    private final int resId, guestId;
    private final double totalAmount, stayCost;
    private final double tax, discount;
    private PaymentStatus status;
    private LocalDateTime createdAt;

    public BillDTO(int id, int resId, int guestId, double stayCost,double totalAmount, double tax, double discount) {
        this.id = id;
        this.resId = resId;
        this.guestId = guestId;
        this.stayCost = stayCost;
        this.totalAmount = totalAmount;
        this.tax = tax;
        this.discount = discount;
    }

    public BillDTO(int id, int resId, int guestId, double totalAmount, double tax, double stayCost, double discount, PaymentStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.resId = resId;
        this.guestId = guestId;
        this.totalAmount = totalAmount;
        this.tax = tax;
        this.stayCost = stayCost;
        this.discount = discount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getResId() {
        return resId;
    }

    public int getGuestId() {
        return guestId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getTax() {
        return tax;
    }

    public double getDiscount() {
        return discount;
    }

    public PaymentStatus getStatus() {
        return status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getStayCost() {
        return stayCost;
    }
}
