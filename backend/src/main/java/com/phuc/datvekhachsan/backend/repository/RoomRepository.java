package com.phuc.datvekhachsan.backend.repository;

import com.phuc.datvekhachsan.backend.model.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> findByHotelId(Long hotelId);
}
