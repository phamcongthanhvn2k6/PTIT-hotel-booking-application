package com.phuc.datvekhachsan.backend.controller;

import com.phuc.datvekhachsan.backend.model.HotelEntity;
import com.phuc.datvekhachsan.backend.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @GetMapping
    public ResponseEntity<List<HotelEntity>> getAllHotels() {
        return ResponseEntity.ok(hotelRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelEntity> getHotelById(@PathVariable Long id) {
        return hotelRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<HotelEntity>> searchHotels(@RequestParam String keyword) {
        return ResponseEntity.ok(hotelRepository.searchHotels(keyword));
    }
}
