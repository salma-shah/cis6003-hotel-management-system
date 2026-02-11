package entity;

import constant.BeddingTypes;
import constant.RoomStatus;
import constant.RoomTypes;

import java.util.Date;

public class Room implements SuperEntity {
    private final int roomId;
    private final double basePricePerNight;
    private final String baseDescription;
    private final BeddingTypes bedding;
    private final RoomTypes roomType;
    private final RoomStatus roomStatus;
    private final int maxOccupancy, floorNum;
    private final Date createdAt;
    private final Date updatedAt;

    // constructor
    public Room(int roomId, double basePricePerNight, String baseDescription, BeddingTypes bedding, RoomTypes roomType, RoomStatus roomStatus, int maxOccupancy, int floorNum, Date createdAt, Date updatedAt) {
        this.roomId = roomId;
        this.basePricePerNight = basePricePerNight;
        this.baseDescription = baseDescription;
        this.bedding = bedding;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.maxOccupancy = maxOccupancy;
        this.floorNum = floorNum;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // getters
    // we  apply the builder pattern
    public int getRoomId() {
        return roomId;
    }

    public double getBasePricePerNight() {
        return basePricePerNight;
    }

    public String getBaseDescription() {
        return baseDescription;
    }

    public RoomTypes getRoomType() {
        return roomType;
    }

    public BeddingTypes getBedding() {
        return bedding;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", basePricePerNight=" + basePricePerNight +
                ", baseDescription='" + baseDescription + '\'' +
                ", bedding=" + bedding +
                ", roomType=" + roomType +
                ", roomStatus=" + roomStatus +
                ", maxOccupancy=" + maxOccupancy +
                ", floorNum=" + floorNum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // builder pattern
    private Room (RoomBuilder builder)
    {
        this.roomId = builder.roomId;
        this.basePricePerNight = builder.basePricePerNight;
        this.baseDescription = builder.baseDescription;
        this.bedding = builder.bedding;
        this.roomType = builder.roomType;
        this.roomStatus = builder.roomStatus;
        this.maxOccupancy = builder.maxOccupancy;
        this.floorNum = builder.floorNum;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    // public static class builder
    public static class RoomBuilder
    {
        private int roomId;
        private double basePricePerNight;
        private String baseDescription;
        private BeddingTypes bedding;
        private RoomTypes roomType;
        private RoomStatus roomStatus;
        private int maxOccupancy;
        private int floorNum;
        private Date createdAt;
        private Date updatedAt;

        public RoomBuilder(){}
        public RoomBuilder roomId(int roomId)
        {
            this.roomId = roomId;
            return this;
        }
        public RoomBuilder basePricePerNight(double basePricePerNight)
        {
            this.basePricePerNight = basePricePerNight;
            return this;
        }
        public RoomBuilder baseDescription(String baseDescription)
        {
            this.baseDescription = baseDescription;
            return this;
        }
        public RoomBuilder bedding(BeddingTypes bedding)
        {
            this.bedding = bedding;
            return this;
        }
        public RoomBuilder roomType(RoomTypes roomType)
        {
            this.roomType = roomType;
            return this;
        }
        public RoomBuilder roomStatus(RoomStatus roomStatus)
        {
            this.roomStatus = roomStatus;

        return this;}
        public RoomBuilder floorNum(int floorNum)
        {
            this.floorNum = floorNum;
            return this;
        }
        public RoomBuilder maxOccupancy(int maxOccupancy)
        {
            this.maxOccupancy = maxOccupancy;
            return this;
        }
        public RoomBuilder createdAt(Date createdAt)
        {
            this.createdAt = createdAt;
            return this;
        }
        public RoomBuilder updatedAt(Date updatedAt)
        {
            this.updatedAt = updatedAt;
            return this;
        }
        public Room build()
        {
            return new Room(this);
        }
    }
}
