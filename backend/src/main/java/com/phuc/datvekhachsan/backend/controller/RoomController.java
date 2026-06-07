package com.phuc.datvekhachsan.backend.controller;

import com.phuc.datvekhachsan.backend.model.RoomEntity;
import com.phuc.datvekhachsan.backend.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private com.phuc.datvekhachsan.backend.repository.BookingRepository bookingRepository;

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomEntity>> getRoomsByHotelId(
            @PathVariable Long hotelId,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.util.Date checkInDate) {
        
        List<RoomEntity> rooms = roomRepository.findByHotelId(hotelId);

        if (checkInDate != null) {
            for (RoomEntity room : rooms) {
                // Determine availability dynamically based on bookings
                boolean isBooked = false;
                List<com.phuc.datvekhachsan.backend.model.BookingEntity> bookings = bookingRepository.findByRoomId(room.getId());
                for (com.phuc.datvekhachsan.backend.model.BookingEntity b : bookings) {
                    if ("CONFIRMED".equals(b.getStatus())) {
                        // A booking spans from checkInDate to checkOutDate
                        // If requested checkInDate falls exactly in this period (>= checkIn and < checkOut)
                        if (!checkInDate.before(b.getCheckInDate()) && checkInDate.before(b.getCheckOutDate())) {
                            isBooked = true;
                            break;
                        }
                    }
                }
                
                if (isBooked) {
                    room.setStatus("UNAVAILABLE");
                } else if ("UNAVAILABLE".equals(room.getStatus())) {
                    // Reset to AVAILABLE for response if physically available but no booking
                    // Assuming all rooms are normally AVAILABLE unless under maintenance, which isn't fully implemented
                    room.setStatus("AVAILABLE");
                }
            }
        }

        return ResponseEntity.ok(rooms);
    }
}
