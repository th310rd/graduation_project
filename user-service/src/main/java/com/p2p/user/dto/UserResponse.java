package com.p2p.user.dto;

import com.p2p.user.entity.UserStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
