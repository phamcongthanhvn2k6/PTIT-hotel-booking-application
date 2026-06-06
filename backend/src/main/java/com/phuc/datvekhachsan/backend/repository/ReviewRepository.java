package com.phuc.datvekhachsan.backend.repository;

import com.phuc.datvekhachsan.backend.model.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByHotelIdOrderByCreatedAtDesc(Long hotelId);
}
