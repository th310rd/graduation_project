package com.p2p.user.dto;

import com.p2p.user.entity.UserRole;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

public record RegisterRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @NotBlank String phoneNumber,
        @NotNull LocalDate dateOfBirth,
        @NotBlank String password,
        Set<UserRole> roles
) {}
