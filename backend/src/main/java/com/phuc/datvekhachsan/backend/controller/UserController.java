package com.phuc.datvekhachsan.backend.controller;

import com.phuc.datvekhachsan.backend.model.HotelEntity;
import com.phuc.datvekhachsan.backend.model.UserEntity;
import com.phuc.datvekhachsan.backend.model.FavoriteHotelEntity;
import com.phuc.datvekhachsan.backend.repository.UserRepository;
import com.phuc.datvekhachsan.backend.repository.HotelRepository;
import com.phuc.datvekhachsan.backend.repository.FavoriteHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private FavoriteHotelRepository favoriteHotelRepository;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("fullName", user.getFullName());
        response.put("phone", user.getPhone());
        response.put("role", user.getRole());
        response.put("avatarUrl", user.getAvatarUrl());
        response.put("status", user.getStatus());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody Map<String, String> request) {
        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        if (request.containsKey("fullName")) {
            user.setFullName(request.get("fullName"));
        }
        if (request.containsKey("phone")) {
            user.setPhone(request.get("phone"));
        }
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Cập nhật thành công"));
    }

    @Autowired
    private com.phuc.datvekhachsan.backend.service.CloudinaryService cloudinaryService;

    @PostMapping("/profile/avatar")
    public ResponseEntity<?> uploadAvatar(Authentication authentication, @RequestParam("file") MultipartFile file) {
        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        try {
            String fileUrl = cloudinaryService.uploadFile(file);
            user.setAvatarUrl(fileUrl);
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("avatarUrl", fileUrl, "message", "Tải ảnh lên thành công"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Lỗi upload: " + e.getMessage()));
        }
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<HotelEntity>> getFavoriteHotels(Authentication authentication) {
        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return ResponseEntity.badRequest().build();

        List<HotelEntity> hotels = favoriteHotelRepository.findHotelsByUserId(user.getId());

        return ResponseEntity.ok(hotels);
    }

    @PostMapping("/favorites/{hotelId}")
    public ResponseEntity<?> addFavoriteHotel(Authentication authentication, @PathVariable Long hotelId) {
        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        HotelEntity hotel = hotelRepository.findById(hotelId).orElse(null);

        if (user == null || hotel == null) return ResponseEntity.badRequest().build();

        if (!favoriteHotelRepository.existsByUserIdAndHotelId(user.getId(), hotelId)) {
            FavoriteHotelEntity favorite = new FavoriteHotelEntity();
            favorite.setUser(user);
            favorite.setHotel(hotel);
            favoriteHotelRepository.save(favorite);
        }

        return ResponseEntity.ok(Map.of("message", "Đã thêm vào mục yêu thích"));
    }

    @DeleteMapping("/favorites/{hotelId}")
    public ResponseEntity<?> removeFavoriteHotel(Authentication authentication, @PathVariable Long hotelId) {
        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        if (user == null) return ResponseEntity.badRequest().build();

        favoriteHotelRepository.deleteByUserIdAndHotelId(user.getId(), hotelId);
        return ResponseEntity.ok(Map.of("message", "Đã xóa khỏi mục yêu thích"));
    }
}
