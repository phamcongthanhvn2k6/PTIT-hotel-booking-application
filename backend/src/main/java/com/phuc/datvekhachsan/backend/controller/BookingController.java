package com.phuc.datvekhachsan.backend.controller;

import com.phuc.datvekhachsan.backend.dto.BookingRequest;
import com.phuc.datvekhachsan.backend.model.BookingEntity;
import com.phuc.datvekhachsan.backend.model.HotelEntity;
import com.phuc.datvekhachsan.backend.model.UserEntity;
import com.phuc.datvekhachsan.backend.repository.BookingRepository;
import com.phuc.datvekhachsan.backend.repository.HotelRepository;
import com.phuc.datvekhachsan.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.phuc.datvekhachsan.backend.model.RoomEntity;
import com.phuc.datvekhachsan.backend.model.UserEntity;
import com.phuc.datvekhachsan.backend.repository.RoomRepository;
import com.phuc.datvekhachsan.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        Optional<RoomEntity> roomOpt = roomRepository.findById(request.getRoomId());

        if (userOpt.isPresent() && roomOpt.isPresent()) {
            RoomEntity room = roomOpt.get();
            
            // FIXME: Cần thêm logic kiểm tra phòng có trống trong ngày không ở đây.
            // Giả sử luôn trống trong phiên bản học tập.
            
            BookingEntity booking = new BookingEntity();
            booking.setUser(userOpt.get());
            booking.setRoom(room);
            booking.setCheckInDate(request.getCheckInDate());
            booking.setCheckOutDate(request.getCheckOutDate());
            booking.setTotalPrice(request.getTotalPrice());
            booking.setStatus("CONFIRMED");

            bookingRepository.save(booking);
            return ResponseEntity.ok("Đặt phòng thành công");
        }
        return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ");
    }

    @GetMapping("/my-history")
    public ResponseEntity<List<BookingEntity>> getMyHistory() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId()));
        }
        return ResponseEntity.status(401).build();
    }
}
