package entity;

import constant.PaymentStatus;

import java.time.LocalDateTime;

public class Bill implements SuperEntity {
    private final int id;
    private final int resId, guestId;
    private final double totalAmount, stayCost;
    private final double tax, discount;
    private PaymentStatus status;
    private LocalDateTime createdAt;

    public Bill(int id, int resId, int guestId, double stayCost, double tax, double discount,double totalAmount) {
        this.id = id;
        this.resId = resId;
        this.guestId = guestId;
        this.stayCost = stayCost;
        this.tax = tax;
        this.discount = discount;
        this.totalAmount = totalAmount;
    }

    public Bill(int id, int resId, int guestId, double totalAmount, double tax, double discount,double stayCost, PaymentStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.resId = resId;
        this.guestId = guestId;
        this.totalAmount = totalAmount;
        this.tax = tax;
        this.discount = discount;
        this.stayCost = stayCost;
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
