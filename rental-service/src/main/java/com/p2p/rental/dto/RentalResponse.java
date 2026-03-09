package com.p2p.rental.dto;

import com.p2p.rental.entity.RentalStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record RentalResponse(
        UUID id,
        UUID vehicleId,
        UUID guestId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Long pickupMileage,
        Long returnMileage,
        RentalStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
