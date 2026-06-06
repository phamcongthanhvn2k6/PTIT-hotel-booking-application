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

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomEntity>> getRoomsByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomRepository.findByHotelId(hotelId));
    }
}
