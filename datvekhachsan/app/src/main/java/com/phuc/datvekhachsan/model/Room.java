package com.phuc.datvekhachsan.model;

public class Room {
    public enum RoomStatus { AVAILABLE, SELECTED, UNAVAILABLE }

    private RoomStatus status;
    @com.google.gson.annotations.SerializedName("roomNumber")
    private String name;

    public Room(RoomStatus status, String name) {
        this.status = status;
        this.name = name;
    }

    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    private Long id;
    private String roomType;
    private double pricePerNight;
    private Hotel hotel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
}
