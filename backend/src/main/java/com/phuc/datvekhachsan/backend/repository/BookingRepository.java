package com.phuc.datvekhachsan.backend.repository;

import com.phuc.datvekhachsan.backend.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<BookingEntity> findByRoomId(Long roomId);
}
