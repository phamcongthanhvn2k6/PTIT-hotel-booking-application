package com.phuc.datvekhachsan.backend.repository;

import com.phuc.datvekhachsan.backend.model.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
    
    @Query("SELECT h FROM HotelEntity h WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(h.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(h.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<HotelEntity> searchHotels(String keyword);
}
