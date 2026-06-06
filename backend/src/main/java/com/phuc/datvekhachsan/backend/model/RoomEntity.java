package com.phuc.datvekhachsan.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonIgnore
    private HotelEntity hotel;

    @Column(name = "room_number", nullable = false, length = 20)
    private String roomNumber;

    @Column(name = "room_type", nullable = false, length = 50)
    private String roomType;

    @Column(name = "price_per_night", nullable = false)
    private Double pricePerNight;

    @Column(length = 20)
    private String status = "AVAILABLE";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public HotelEntity getHotel() { return hotel; }
    public void setHotel(HotelEntity hotel) { this.hotel = hotel; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public Double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(Double pricePerNight) { this.pricePerNight = pricePerNight; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
