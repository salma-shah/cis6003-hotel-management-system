package entity;

import constant.BeddingTypes;
import dto.SuperDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomType implements SuperEntity, SuperDTO {
    private final int roomTypeId;
    private final String roomTypeName;
    private final double basePricePerNight;
    private final String baseDescription;
    private final BeddingTypes bedding;
    private final int maxOccupancy, totalRooms;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<RoomImg> roomImgList;
    private final List<Amenity> amenityList;

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

    public List<RoomImg> getRoomImgList() {
        return roomImgList;
    }

    public List<Amenity> getAmenityList() {
        return amenityList;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "roomTypeId=" + roomTypeId +
                ", roomTypeName='" + roomTypeName + '\'' +
                ", basePricePerNight=" + basePricePerNight +
                ", baseDescription='" + baseDescription + '\'' +
                ", bedding=" + bedding +
                ", maxOccupancy=" + maxOccupancy +
                ", totalRooms=" + totalRooms +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", roomImgList=" + roomImgList +
                ", amenityList=" + amenityList +
                '}';
    }

    public RoomType(int roomTypeId, String roomTypeName, double basePricePerNight, String baseDescription, BeddingTypes bedding, int maxOccupancy, int totalRooms, LocalDateTime createdAt, LocalDateTime updatedAt, List<RoomImg> roomImgList, List<Amenity> amenityList) {
        this.roomTypeId = roomTypeId;
        this.roomTypeName = roomTypeName;
        this.basePricePerNight = basePricePerNight;
        this.baseDescription = baseDescription;
        this.bedding = bedding;
        this.maxOccupancy = maxOccupancy;
        this.totalRooms = totalRooms;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roomImgList = roomImgList;
        this.amenityList = amenityList;
    }

    private RoomType(RoomTypeBuilder builder) {
        this.roomTypeId = builder.roomTypeId;
        this.roomTypeName = builder.roomTypeName;
        this.basePricePerNight = builder.basePricePerNight;
        this.baseDescription = builder.baseDescription;
        this.maxOccupancy = builder.maxOccupancy;
        this.totalRooms = builder.totalRooms;
        this.bedding = builder.bedding;
        this.roomImgList = builder.roomImgList != null
                ? builder.roomImgList
                : new ArrayList<>();
        this.amenityList = builder.amenityList != null
                ? builder.amenityList
                : new ArrayList<>();
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static class RoomTypeBuilder {
        private int roomTypeId;
        private String roomTypeName;
        private double basePricePerNight;
        private String baseDescription;
        private BeddingTypes bedding;
        private int maxOccupancy;
        private int totalRooms;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<Amenity> amenityList;
        private List<RoomImg> roomImgList;

        public RoomTypeBuilder roomTypeId(int roomTypeId) {
            this.roomTypeId = roomTypeId;
            return this;
        }

        public RoomTypeBuilder roomTypeName(String roomTypeName) {
            this.roomTypeName = roomTypeName;
            return this;
        }

        public RoomTypeBuilder basePricePerNight(double price) {
            this.basePricePerNight = price;
            return this;
        }

        public RoomTypeBuilder baseDescription(String desc) {
            this.baseDescription = desc;
            return this;
        }

        public RoomTypeBuilder bedding(BeddingTypes bedding) {
            this.bedding = bedding;
            return this;
        }

        public RoomTypeBuilder maxOccupancy(int max) {
            this.maxOccupancy = max;
            return this;
        }

        public RoomTypeBuilder totalRooms(int total) {
            this.totalRooms = total;
            return this;
        }

        public RoomTypeBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RoomTypeBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RoomTypeBuilder amenityList(List<Amenity> amenities) {
            this.amenityList = amenities;
            return this;
        }

        public RoomTypeBuilder roomImgList(List<RoomImg> images) {
            this.roomImgList = images;
            return this;
        }

        public RoomType build() {
            return new RoomType(this);
        }
    }

}
