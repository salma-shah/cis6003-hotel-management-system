package dto;

import constant.RoomStatus;

import java.time.LocalDateTime;

public class RoomDTO implements SuperDTO {
    private final int roomId;
    private final RoomTypeDTO roomType;
    private final RoomStatus roomStatus;
    private final String roomNum;
    private final int floorNum;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public RoomDTO(RoomDTOBuilder roomDTOBuilder) {
        this.roomId = roomDTOBuilder.roomId;
        this.roomNum = roomDTOBuilder.roomNum;
        this.roomType = roomDTOBuilder.roomType;
        this.roomStatus = roomDTOBuilder.roomStatus;
        this.floorNum = roomDTOBuilder.floorNum;
        this.createdAt = roomDTOBuilder.createdAt;
        this.updatedAt = roomDTOBuilder.updatedAt;
    }

    public int getRoomId() {
        return roomId;
    }

   public String getRoomNum() {
        return roomNum;
   }

    public RoomTypeDTO getRoomType() {
        return roomType;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
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

    @Override
    public String toString() {
        return "RoomDTO{" +
                "roomId=" + roomId +
               ",roomNum='" + roomNum + '\'' +
                ", roomType=" + roomType +
                ", roomStatus=" + roomStatus +
                ", floorNum=" + floorNum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }


    // public static class builder
    public static class RoomDTOBuilder {
        private int roomId;
        private String roomNum;
        private RoomTypeDTO roomType;
        private RoomStatus roomStatus;
        private int floorNum;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public RoomDTOBuilder() {
        }

        public RoomDTO.RoomDTOBuilder roomId(int roomId) {
            this.roomId = roomId;
            return this;
        }

        public RoomDTO.RoomDTOBuilder roomType(RoomTypeDTO roomType) {
            this.roomType = roomType;
            return this;
        }

        public RoomDTO.RoomDTOBuilder roomNum(String roomNum) {
            this.roomNum = roomNum;
            return this;
        }

        public RoomDTO.RoomDTOBuilder roomStatus(RoomStatus roomStatus) {
            this.roomStatus = roomStatus;
            return this;
        }

        public RoomDTO.RoomDTOBuilder floorNum(int floorNum) {
            this.floorNum = floorNum;
            return this;
        }

        public RoomDTO.RoomDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RoomDTO.RoomDTOBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RoomDTO build() {
            return new RoomDTO(this);
        }
    }
}
