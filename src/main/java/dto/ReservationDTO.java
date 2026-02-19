package dto;

import constant.ReservationStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class ReservationDTO implements Serializable {
    private final int id;
    private final int guestId;
    private final int roomId;
    private final String reservationNumber;
    private final double totalCost;
    private final int numOfAdults, numOfChildren;
    private final LocalDateTime dateOfReservation;
    private final Date checkInDate, checkOutDate;
    private final ReservationStatus status;
    private LocalDateTime createdAt, updatedAt;

    public ReservationDTO(int id, int guestId, int roomId, String reservationNumber, double totalCost, LocalDateTime dateOfReservation, Date checkInDate, Date checkOutDate, int numOfAdults, int numOfChildren, ReservationStatus status) {
        this.id = id;
        this.guestId = guestId;
        this.roomId = roomId;
        this.reservationNumber = reservationNumber;
        this.totalCost = totalCost;
        this.dateOfReservation = dateOfReservation;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numOfAdults = numOfAdults;
        this.numOfChildren = numOfChildren;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getGuestId() {
        return guestId;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public LocalDateTime getDateOfReservation() {
        return dateOfReservation;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public int getNumOfAdults() {
        return numOfAdults;
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
