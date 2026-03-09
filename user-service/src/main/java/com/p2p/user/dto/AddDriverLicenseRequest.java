package com.p2p.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AddDriverLicenseRequest(
        @NotBlank String licenseNumber,
        @NotBlank String category,
        @NotNull LocalDate issuedDate,
        @NotNull LocalDate expiryDate,
        @NotBlank String issuedBy,
        @NotBlank String countryCode
) {}
