package com.phuc.datvekhachsan.backend.controller;

import com.phuc.datvekhachsan.backend.model.*;
import com.phuc.datvekhachsan.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    // --- DASHBOARD STATS ---
    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getStats() {
        long totalUsers = userRepository.count();
        long totalHotels = hotelRepository.count();
        long totalBookings = bookingRepository.count();
        
        List<BookingEntity> bookings = bookingRepository.findAll();
        double totalRevenue = bookings.stream()
                .filter(b -> "COMPLETED".equals(b.getStatus()))
                .mapToDouble(BookingEntity::getTotalPrice)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalHotels", totalHotels);
        stats.put("totalBookings", totalBookings);
        stats.put("totalRevenue", totalRevenue);

        return ResponseEntity.ok(stats);
    }

    // --- USERS ---
    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setRole(request.get("role"));
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Cập nhật quyền thành công"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setStatus(request.get("status"));
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái thành công"));
        }
        return ResponseEntity.notFound().build();
    }

    // --- HOTELS ---
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelEntity>> getAllHotels() {
        return ResponseEntity.ok(hotelRepository.findAll());
    }

    @PostMapping("/hotels")
    public ResponseEntity<?> addHotel(@RequestBody HotelEntity hotel) {
        HotelEntity saved = hotelRepository.save(hotel);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/hotels/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable Long id, @RequestBody HotelEntity hotel) {
        Optional<HotelEntity> existing = hotelRepository.findById(id);
        if (existing.isPresent()) {
            HotelEntity h = existing.get();
            h.setName(hotel.getName());
            h.setLocation(hotel.getLocation());
            h.setDescription(hotel.getDescription());
            h.setPrice(hotel.getPrice());
            h.setImageUrl(hotel.getImageUrl());
            // Update other fields as necessary
            hotelRepository.save(h);
            return ResponseEntity.ok(h);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/hotels/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable Long id) {
        hotelRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Xóa khách sạn thành công"));
    }

    // --- ROOMS ---
    @GetMapping("/hotels/{hotelId}/rooms")
    public ResponseEntity<List<RoomEntity>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomRepository.findByHotelId(hotelId));
    }

    @PostMapping("/hotels/{hotelId}/rooms")
    public ResponseEntity<?> addRoom(@PathVariable Long hotelId, @RequestBody RoomEntity room) {
        Optional<HotelEntity> hotel = hotelRepository.findById(hotelId);
        if (hotel.isPresent()) {
            room.setHotel(hotel.get());
            return ResponseEntity.ok(roomRepository.save(room));
        }
        return ResponseEntity.badRequest().body("Khách sạn không tồn tại");
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody RoomEntity room) {
        Optional<RoomEntity> existing = roomRepository.findById(id);
        if (existing.isPresent()) {
            RoomEntity r = existing.get();
            r.setRoomNumber(room.getRoomNumber());
            r.setRoomType(room.getRoomType());
            r.setPricePerNight(room.getPricePerNight());
            r.setStatus(room.getStatus());
            roomRepository.save(r);
            return ResponseEntity.ok(r);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        roomRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Xóa phòng thành công"));
    }

    // --- BOOKINGS ---
    @GetMapping("/bookings")
    public ResponseEntity<List<BookingEntity>> getAllBookings() {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @PutMapping("/bookings/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Optional<BookingEntity> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent()) {
            BookingEntity booking = bookingOpt.get();
            booking.setStatus(request.get("status"));
            bookingRepository.save(booking);
            return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái thành công"));
        }
        return ResponseEntity.notFound().build();
    }

    @Autowired
    private com.phuc.datvekhachsan.backend.service.CloudinaryService cloudinaryService;

    // --- FILE UPLOAD ---
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = cloudinaryService.uploadFile(file);
            return ResponseEntity.ok(Map.of("url", fileUrl));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Lỗi upload: " + e.getMessage()));
        }
    }
}
