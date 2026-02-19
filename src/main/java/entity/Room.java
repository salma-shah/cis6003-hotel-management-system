package entity;

import constant.RoomStatus;
import java.time.LocalDateTime;

public class Room implements SuperEntity {
    private final int roomId;
    private final RoomType roomType;
    private final RoomStatus roomStatus;
    private final String roomNum;
    private final int floorNum;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public int getRoomId() {
        return roomId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // constructor


    public Room(int roomId, RoomType roomType, RoomStatus roomStatus, String roomNum, int floorNum, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.roomNum = roomNum;
        this.floorNum = floorNum;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomType=" + roomType +
                ", roomStatus=" + roomStatus +
                ", roomNum='" + roomNum + '\'' +
                ", floorNum=" + floorNum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // builder pattern
    private Room (RoomBuilder builder)
    {
        this.roomId = builder.roomId;
        this.roomType = builder.roomType;
        this.roomStatus = builder.roomStatus;
        this.roomNum = builder.roomNum;
        this.floorNum = builder.floorNum;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;

    }

    // we  apply the builder pattern
    // public static class builder
    public static class RoomBuilder
    {
        private int roomId;
        private RoomType roomType;
        private RoomStatus roomStatus;
        private int floorNum;
        private String roomNum;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public RoomBuilder(){}
        public RoomBuilder roomId(int roomId)
        {
            this.roomId = roomId;
            return this;
        }
       public RoomBuilder roomNum (String roomNum)
       {
           this.roomNum = roomNum;
           return this;
       }
        public RoomBuilder roomType(RoomType roomType)
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

        public RoomBuilder createdAt(LocalDateTime createdAt)
        {
            this.createdAt = createdAt;
            return this;
        }
        public RoomBuilder updatedAt(LocalDateTime updatedAt)
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
