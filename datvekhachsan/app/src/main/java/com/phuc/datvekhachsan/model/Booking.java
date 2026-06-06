package com.phuc.datvekhachsan.model;

import java.io.Serializable;

public class Booking implements Serializable {
    private final String hotelName;
    private final int hotelImageResId;
    private final String hotelLocation;
    private final String roomNames;
    private final String roomType;
    private final String checkInDate;
    private final double totalPrice;
    private final long bookingTime;

    public Booking(String hotelName, int hotelImageResId, String hotelLocation, String roomNames,
                   String roomType, String checkInDate, double totalPrice, long bookingTime) {
        this.hotelName = hotelName;
        this.hotelImageResId = hotelImageResId;
        this.hotelLocation = hotelLocation;
        this.roomNames = roomNames;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.totalPrice = totalPrice;
        this.bookingTime = bookingTime;
    }

    public String getHotelName() {
        return hotelName;
    }

    public int getHotelImageResId() {
        return hotelImageResId;
    }

    public String getHotelLocation() {
        return hotelLocation;
    }

    public String getRoomNames() {
        return roomNames;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public long getBookingTime() {
        return bookingTime;
    }
}
