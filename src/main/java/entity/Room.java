package entity;

import constant.BeddingTypes;
import constant.RoomStatus;
import constant.RoomTypes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Room implements SuperEntity {
    private final int roomId;
    private final double basePricePerNight;
    private final String baseDescription;
    private final BeddingTypes bedding;
    private final RoomTypes roomType;
    private final RoomStatus roomStatus;
    private final int maxOccupancy, floorNum, totalRooms;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<RoomImg> roomImgList;
    private final List<Amenity> amenityList;

    public int getRoomId() {
        return roomId;
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

    public RoomTypes getRoomType() {
        return roomType;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public int getFloorNum() {
        return floorNum;
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

    // constructor
    public Room(int roomId, double basePricePerNight, String baseDescription, BeddingTypes bedding, RoomTypes roomType, RoomStatus roomStatus, int maxOccupancy, int totalRooms, int floorNum, LocalDateTime createdAt, LocalDateTime updatedAt, List<RoomImg> roomImgList, List<Amenity> amenityList) {
        this.roomId = roomId;
        this.basePricePerNight = basePricePerNight;
        this.baseDescription = baseDescription;
        this.bedding = bedding;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.maxOccupancy = maxOccupancy;
        this.floorNum = floorNum;
        this.totalRooms = totalRooms;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roomImgList = roomImgList;
        this.amenityList = amenityList;
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
                ", totalRooms=" + totalRooms +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", roomImgList=" + roomImgList +
                ", amenityList=" + amenityList +
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
        this.totalRooms = builder.totalRooms;
        this.maxOccupancy = builder.maxOccupancy;
        this.floorNum = builder.floorNum;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.roomImgList = builder.roomImgList != null
                ? builder.roomImgList
                : new ArrayList<>();
        this.amenityList = builder.amenityList != null
                ? builder.amenityList
                : new ArrayList<>();

    }

    // we  apply the builder pattern
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
        private int floorNum, totalRooms;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<RoomImg>  roomImgList;
        private  List<Amenity> amenityList;

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
        public RoomBuilder roomImgList(List<RoomImg> roomImgList)
        {
            this.roomImgList = roomImgList;
            return this;
        }

        public RoomBuilder amenityList(List<Amenity>  amenityList)
        {
            this.amenityList = amenityList;
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

        public RoomBuilder totalRooms(int totalRooms)
        {
            this.totalRooms = totalRooms;
            return this;
        }

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
