package com.p2p.listing.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateListingRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String city,
        @NotNull @DecimalMin("0.01") BigDecimal pricePerDay,
        @NotNull @DecimalMin("0.00") BigDecimal depositAmount,
        @NotNull Boolean instantBooking,
        @NotNull Boolean deliveryAvailable
) {
}
