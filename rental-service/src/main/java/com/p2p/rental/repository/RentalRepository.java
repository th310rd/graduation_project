package com.p2p.rental.repository;

import com.p2p.rental.entity.Rental;
import com.p2p.rental.entity.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RentalRepository extends JpaRepository<Rental, UUID> {
    @Query("""
            select count(r) > 0 from Rental r
            where r.vehicleId = :vehicleId
              and r.status in :statuses
              and :startDate < r.endDateTime
              and :endDate > r.startDateTime
            """)
    boolean existsOverlapping(@Param("vehicleId") UUID vehicleId,
                              @Param("startDate") LocalDateTime startDate,
                              @Param("endDate") LocalDateTime endDate,
                              @Param("statuses") List<RentalStatus> statuses);
}
