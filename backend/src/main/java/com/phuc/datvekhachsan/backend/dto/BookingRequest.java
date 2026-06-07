package com.phuc.datvekhachsan.backend.dto;

import java.util.Date;

public class BookingRequest {
    private Long roomId; // Keep for backward compatibility
    private java.util.List<Long> roomIds;
    private Date checkInDate;
    private Date checkOutDate;
    private Double totalPrice;

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public java.util.List<Long> getRoomIds() { return roomIds; }
    public void setRoomIds(java.util.List<Long> roomIds) { this.roomIds = roomIds; }
    public Date getCheckInDate() { return checkInDate; }
    public void setCheckInDate(Date checkInDate) { this.checkInDate = checkInDate; }
    public Date getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(Date checkOutDate) { this.checkOutDate = checkOutDate; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}
