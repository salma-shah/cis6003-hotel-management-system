package dto;

import java.util.Map;

public class ReportDTO {

    // reservation summary
    private final int totalReservations;
    private final int confirmedReservations;
    private final int checkedInReservations;
    private final int cancelledReservations;

    // revenue
    private final double totalNetRevenue;
    private final double totalStayRevenue;
    private final double totalTax;

    // guests
    private final int newGuests;
    private final int returnedGuests;

    // payments
    private final double paidAmounts;
    private final double outstandingAmounts;
    private final int paidBills;
    private final int outstandingBills;

    public ReportDTO(int totalReservations, int confirmedReservations, int checkedInReservations,
                     int cancelledReservations, double totalNetRevenue, double totalStayRevenue, double totalTax, int newGuests, int returnedGuests, double paidAmounts, double outstandingAmounts, int paidBills, int outstandingBills) {
        this.totalReservations = totalReservations;
        this.confirmedReservations = confirmedReservations;
        this.checkedInReservations = checkedInReservations;
        this.cancelledReservations = cancelledReservations;
        this.totalNetRevenue = totalNetRevenue;
        this.totalStayRevenue = totalStayRevenue;
        this.totalTax = totalTax;
        this.newGuests = newGuests;
        this.returnedGuests = returnedGuests;
        this.paidAmounts = paidAmounts;
        this.outstandingAmounts = outstandingAmounts;
        this.paidBills = paidBills;
        this.outstandingBills = outstandingBills;
    }

    public int getTotalReservations() {
        return totalReservations;
    }

    public int getConfirmedReservations() {
        return confirmedReservations;
    }

    public int getCheckedInReservations() {
        return checkedInReservations;
    }

    public int getCancelledReservations() {
        return cancelledReservations;
    }

    public double getTotalNetRevenue() {
        return totalNetRevenue;
    }

    public double getTotalStayRevenue() {
        return totalStayRevenue;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public int getNewGuests() {
        return newGuests;
    }

    public int getReturnedGuests() {
        return returnedGuests;
    }

    public double getPaidAmounts() {
        return paidAmounts;
    }

    public double getOutstandingAmounts() {
        return outstandingAmounts;
    }

    public int getPaidBills() {
        return paidBills;
    }

    public int getOutstandingBills() {
        return outstandingBills;
    }
}
