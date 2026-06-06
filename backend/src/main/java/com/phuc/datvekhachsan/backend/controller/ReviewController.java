package com.phuc.datvekhachsan.backend.controller;

import com.phuc.datvekhachsan.backend.dto.ReviewRequest;
import com.phuc.datvekhachsan.backend.model.HotelEntity;
import com.phuc.datvekhachsan.backend.model.ReviewEntity;
import com.phuc.datvekhachsan.backend.model.UserEntity;
import com.phuc.datvekhachsan.backend.repository.HotelRepository;
import com.phuc.datvekhachsan.backend.repository.ReviewRepository;
import com.phuc.datvekhachsan.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<ReviewEntity>> getReviewsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(reviewRepository.findByHotelIdOrderByCreatedAtDesc(hotelId));
    }

    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody ReviewRequest request) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        Optional<HotelEntity> hotelOpt = hotelRepository.findById(request.getHotelId());

        if (userOpt.isPresent() && hotelOpt.isPresent()) {
            ReviewEntity review = new ReviewEntity();
            review.setUser(userOpt.get());
            review.setHotel(hotelOpt.get());
            review.setRating(request.getRating());
            review.setComment(request.getComment());

            reviewRepository.save(review);
            
            // Cập nhật lại Rating trung bình của Hotel
            List<ReviewEntity> allReviews = reviewRepository.findByHotelIdOrderByCreatedAtDesc(request.getHotelId());
            double average = allReviews.stream().mapToInt(ReviewEntity::getRating).average().orElse(0.0);
            HotelEntity hotel = hotelOpt.get();
            hotel.setRating((float) average);
            hotelRepository.save(hotel);

            return ResponseEntity.ok("Đánh giá thành công");
        }
        return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ");
    }
}
