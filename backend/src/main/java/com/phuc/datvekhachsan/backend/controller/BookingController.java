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

        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Người dùng không tồn tại");
        }
        
        List<Long> idsToBook = request.getRoomIds() != null && !request.getRoomIds().isEmpty() 
            ? request.getRoomIds() 
            : (request.getRoomId() != null ? java.util.Collections.singletonList(request.getRoomId()) : java.util.Collections.emptyList());

        if (idsToBook.isEmpty()) {
            return ResponseEntity.badRequest().body(java.util.Collections.singletonMap("message", "Chưa chọn phòng"));
        }

        // Check availability first
        for (Long rId : idsToBook) {
            Optional<RoomEntity> roomOpt = roomRepository.findById(rId);
            if (!roomOpt.isPresent()) {
                return ResponseEntity.badRequest().body(java.util.Collections.singletonMap("message", "Phòng không tồn tại"));
            }
            
            // Date-based overlap check
            List<BookingEntity> existingBookings = bookingRepository.findByRoomId(rId);
            for (BookingEntity b : existingBookings) {
                if ("CONFIRMED".equals(b.getStatus())) {
                    // Two intervals [start1, end1] and [start2, end2] overlap if start1 < end2 AND start2 < end1
                    if (request.getCheckInDate().before(b.getCheckOutDate()) && request.getCheckOutDate().after(b.getCheckInDate())) {
                        return ResponseEntity.badRequest().body(java.util.Collections.singletonMap("message", "Phòng " + roomOpt.get().getRoomNumber() + " đã được đặt trong ngày bạn chọn."));
                    }
                }
            }
        }

        double pricePerRoom = request.getTotalPrice() / idsToBook.size();

        for (Long rId : idsToBook) {
            Optional<RoomEntity> roomOpt = roomRepository.findById(rId);
            if (roomOpt.isPresent()) {
                RoomEntity room = roomOpt.get();
                
                BookingEntity booking = new BookingEntity();
                booking.setUser(userOpt.get());
                booking.setRoom(room);
                booking.setCheckInDate(request.getCheckInDate());
                booking.setCheckOutDate(request.getCheckOutDate());
                booking.setTotalPrice(pricePerRoom);
                booking.setStatus("CONFIRMED");

                bookingRepository.save(booking);
            }
        }
        return ResponseEntity.ok(java.util.Collections.singletonMap("message", "Đặt phòng thành công"));
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
