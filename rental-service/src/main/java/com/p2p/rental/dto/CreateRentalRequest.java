package com.p2p.rental.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateRentalRequest(
        @NotNull UUID vehicleId,
        @NotNull UUID guestId,
        @NotNull LocalDateTime startDateTime,
        @NotNull LocalDateTime endDateTime
) {}
