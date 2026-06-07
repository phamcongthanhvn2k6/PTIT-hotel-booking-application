package com.phuc.datvekhachsan.backend.repository;

import com.phuc.datvekhachsan.backend.model.FavoriteHotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.phuc.datvekhachsan.backend.model.HotelEntity;

@Repository
public interface FavoriteHotelRepository extends JpaRepository<FavoriteHotelEntity, Long> {
    List<FavoriteHotelEntity> findByUserId(Long userId);
    
    @Query("SELECT f.hotel FROM FavoriteHotelEntity f WHERE f.user.id = :userId")
    List<HotelEntity> findHotelsByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndHotelId(Long userId, Long hotelId);

    @Transactional
    void deleteByUserIdAndHotelId(Long userId, Long hotelId);
}
