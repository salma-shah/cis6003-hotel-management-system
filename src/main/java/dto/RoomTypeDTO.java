package dto;

import constant.BeddingTypes;

import java.time.LocalDateTime;
import java.util.List;

public class RoomTypeDTO implements SuperDTO {

    private final int roomTypeId;
    private final String roomTypeName;
    private final double basePricePerNight;
    private final String baseDescription;
    private final BeddingTypes bedding;
    private final int maxOccupancy;
    private final int totalRooms;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<RoomImgDTO> roomImgList;
    private final List<AmenityDTO> amenityList;

    private RoomTypeDTO(Builder builder) {
        this.roomTypeId = builder.roomTypeId;
        this.roomTypeName = builder.roomTypeName;
        this.basePricePerNight = builder.basePricePerNight;
        this.baseDescription = builder.baseDescription;
        this.bedding = builder.bedding;
        this.maxOccupancy = builder.maxOccupancy;
        this.totalRooms = builder.totalRooms;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.roomImgList = builder.roomImgList;
        this.amenityList = builder.amenityList;
    }

    // getters
    public int getRoomTypeId() {
        return roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public double getBasePricePerNight() {
        return basePricePerNight;
    }

    public String getBaseDescription() {
        return baseDescription;
    }

    public BeddingTypes getBedding() {
        return bedding;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<RoomImgDTO> getRoomImgList() {
        return roomImgList;
    }

    public List<AmenityDTO> getAmenityList() {
        return amenityList;
    }

    // nested buukder class
    public static class Builder {
        private int roomTypeId;
        private String roomTypeName;
        private double basePricePerNight;
        private String baseDescription;
        private BeddingTypes bedding;
        private int maxOccupancy;
        private int totalRooms;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<RoomImgDTO> roomImgList;
        private List<AmenityDTO> amenityList;

        public Builder roomTypeId(int roomTypeId) {
            this.roomTypeId = roomTypeId;
            return this;
        }

        public Builder roomTypeName(String roomTypeName) {
            this.roomTypeName = roomTypeName;
            return this;
        }

        public Builder basePricePerNight(double basePricePerNight) {
            this.basePricePerNight = basePricePerNight;
            return this;
        }

        public Builder baseDescription(String baseDescription) {
            this.baseDescription = baseDescription;
            return this;
        }

        public Builder bedding(BeddingTypes bedding) {
            this.bedding = bedding;
            return this;
        }

        public Builder maxOccupancy(int maxOccupancy) {
            this.maxOccupancy = maxOccupancy;
            return this;
        }

        public Builder totalRooms(int totalRooms) {
            this.totalRooms = totalRooms;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder roomImgList(List<RoomImgDTO> roomImgList) {
            this.roomImgList = roomImgList;
            return this;
        }

        public Builder amenityList(List<AmenityDTO> amenityList) {
            this.amenityList = amenityList;
            return this;
        }

        public RoomTypeDTO build() {
            return new RoomTypeDTO(this);
        }
    }
}
