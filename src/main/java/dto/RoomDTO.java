package dto;

import constant.BeddingTypes;
import constant.RoomStatus;
import constant.RoomTypes;
import entity.RoomImg;

import java.util.Date;
import java.util.List;

public class RoomDTO implements SuperDTO {
    private final int roomId;
    private final String baseDescription;
    private final double basePricePerNight;
    private final RoomTypes roomType;
    private final RoomStatus roomStatus;
    private final BeddingTypes bedding;
    private final int maxOccupancy, floorNum;
    private final Date createdAt, updatedAt;
    private List<RoomImgDTO> roomImgList;

    public RoomDTO(RoomDTOBuilder roomDTOBuilder) {
        this.roomId = roomDTOBuilder.roomId;
        this.baseDescription = roomDTOBuilder.baseDescription;
        this.basePricePerNight = roomDTOBuilder.basePricePerNight;
        this.roomType = roomDTOBuilder.roomType;
        this.roomStatus = roomDTOBuilder.roomStatus;
        this.bedding = roomDTOBuilder.bedding;
        this.maxOccupancy = roomDTOBuilder.maxOccupancy;
        this.floorNum = roomDTOBuilder.floorNum;
        this.createdAt = roomDTOBuilder.createdAt;
        this.updatedAt = roomDTOBuilder.updatedAt;
        this.roomImgList = roomDTOBuilder.roomImgList;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getDescription() {
        return baseDescription;
    }

    public double getPricePerNight() {
        return basePricePerNight;
    }

    public RoomTypes getRoomType() {
        return roomType;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public BeddingTypes getBeddingTypes() {
        return bedding;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public List<RoomImgDTO> getRoomImgList() { return roomImgList; }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
                "roomId=" + roomId +
                ", baseDescription='" + baseDescription + '\'' +
                ", basePricePerNight=" + basePricePerNight +
                ", roomType=" + roomType +
                ", roomStatus=" + roomStatus +
                ", bedding=" + bedding +
                ", maxOccupancy=" + maxOccupancy +
                ", floorNum=" + floorNum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", roomImgList=" + roomImgList +
                '}';
    }


    // public static class builder
    public static class RoomDTOBuilder {
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
        private List<RoomImgDTO> roomImgList;

        public RoomDTOBuilder() {
        }

        public RoomDTO.RoomDTOBuilder roomId(int roomId) {
            this.roomId = roomId;
            return this;
        }

        public RoomDTO.RoomDTOBuilder basePricePerNight(double basePricePerNight) {
            this.basePricePerNight = basePricePerNight;
            return this;
        }

        public RoomDTO.RoomDTOBuilder roomImgList(List<RoomImgDTO> roomImgList) {
            this.roomImgList = roomImgList;
            return this;
        }
        public RoomDTO.RoomDTOBuilder baseDescription(String baseDescription) {
            this.baseDescription = baseDescription;
            return this;
        }

        public RoomDTO.RoomDTOBuilder bedding(BeddingTypes bedding) {
            this.bedding = bedding;
            return this;
        }

        public RoomDTO.RoomDTOBuilder roomType(RoomTypes roomType) {
            this.roomType = roomType;
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

        public RoomDTO.RoomDTOBuilder maxOccupancy(int maxOccupancy) {
            this.maxOccupancy = maxOccupancy;
            return this;
        }

        public RoomDTO.RoomDTOBuilder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RoomDTO.RoomDTOBuilder updatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RoomDTO build() {
            return new RoomDTO(this);
        }
    }
}
