package com.p2p.user.dto;

import com.p2p.user.entity.UserStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @NotBlank String phoneNumber,
        @NotNull LocalDate dateOfBirth,
        UserStatus status
) {}
