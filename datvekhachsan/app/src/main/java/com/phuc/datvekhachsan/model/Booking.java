package com.phuc.datvekhachsan.model;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {
    private Long id;
    private Long userId;
    private Long roomId;

    @com.google.gson.annotations.SerializedName("user")
    private User user;

    @com.google.gson.annotations.SerializedName("room")
    private Room room;

    @com.google.gson.annotations.SerializedName("checkInDate")
    private Date checkInDateObj;

    @com.google.gson.annotations.SerializedName("checkOutDate")
    private Date checkOutDateObj;

    private double totalPrice;
    private String status;

    // Cũ - Giữ lại để không lỗi các trang cũ
    private String hotelName;
    private int hotelImageResId;
    private String hotelLocation;
    private String roomNames;
    private String roomType;
    
    @com.google.gson.annotations.SerializedName("legacyCheckInDate")
    private String checkInDate;
    
    private long bookingTime;
    
    private java.util.ArrayList<Long> roomIds;

    public Booking() {}

    public Booking(String hotelName, int hotelImageResId, String hotelLocation, String roomNames,
                   String roomType, String checkInDate, double totalPrice, long bookingTime, java.util.ArrayList<Long> roomIds) {
        this.hotelName = hotelName;
        this.hotelImageResId = hotelImageResId;
        this.hotelLocation = hotelLocation;
        this.roomNames = roomNames;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.totalPrice = totalPrice;
        this.bookingTime = bookingTime;
        this.roomIds = roomIds;
    }

    public java.util.ArrayList<Long> getRoomIds() { return roomIds; }
    public void setRoomIds(java.util.ArrayList<Long> roomIds) { this.roomIds = roomIds; }

    public String getHotelName() {
        if (room != null && room.getHotel() != null) return room.getHotel().getName();
        return hotelName;
    }
    public int getHotelImageResId() {
        // Since backend doesn't send ImageResId, fallback to a default image if legacy isn't there
        return hotelImageResId != 0 ? hotelImageResId : com.phuc.datvekhachsan.R.drawable.hotel;
    }
    public String getHotelLocation() {
        if (room != null && room.getHotel() != null) return room.getHotel().getLocation();
        return hotelLocation;
    }
    public String getRoomNames() {
        if (room != null) return "Phòng " + room.getName();
        return roomNames;
    }
    public String getRoomType() {
        if (room != null) return room.getRoomType();
        return roomType;
    }
    @com.google.gson.annotations.SerializedName("createdAt")
    private Date createdAt;

    public String getCheckInDate() {
        if (checkInDateObj != null) {
            return new java.text.SimpleDateFormat("EEE/dd/MMM", new java.util.Locale("vi", "VN")).format(checkInDateObj);
        }
        return checkInDate;
    }
    public long getBookingTime() {
        if (createdAt != null) return createdAt.getTime();
        return bookingTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { 
        if (user != null) return user.getId();
        return userId; 
    }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getRoomId() { 
        if (room != null) return room.getId();
        return roomId; 
    }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public Date getCheckInDateObj() { return checkInDateObj; }
    public void setCheckInDateObj(Date checkInDateObj) { this.checkInDateObj = checkInDateObj; }

    public Date getCheckOutDateObj() { return checkOutDateObj; }
    public void setCheckOutDateObj(Date checkOutDateObj) { this.checkOutDateObj = checkOutDateObj; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
