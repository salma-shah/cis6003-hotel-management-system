package dto;

import constant.ReservationStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationDTO implements Serializable, SuperDTO {
    private final int id;
    private final int guestId;
    private final int roomId;
    private final String reservationNumber;
    private final double totalCost;
    private final int numOfAdults, numOfChildren;
    private final LocalDateTime dateOfReservation;
    private final LocalDate checkInDate, checkOutDate;
    private ReservationStatus status;
    private LocalDateTime createdAt, updatedAt;

    public ReservationDTO(int id, int guestId, int roomId, String reservationNumber, double totalCost, LocalDateTime dateOfReservation, LocalDate checkInDate, LocalDate checkOutDate, int numOfAdults, int numOfChildren, ReservationStatus status) {
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

    public ReservationDTO(int id, int guestId, int roomId, String reservationNum, double totalPrice, LocalDateTime dateOfRes, LocalDate checkInDate, LocalDate checkOutDate, int numAdults, int numChildren) {
    this.id = id;
    this.guestId = guestId;
    this.roomId = roomId;
    this.reservationNumber = reservationNum;
    this.totalCost = totalPrice;
    this.dateOfReservation = dateOfRes;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    this.numOfAdults = numAdults;
    this.numOfChildren = numChildren;
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

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
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
