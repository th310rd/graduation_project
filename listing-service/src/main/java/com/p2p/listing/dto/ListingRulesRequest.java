package com.p2p.listing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ListingRulesRequest(
        @NotNull @Min(18) Integer minDriverAge,
        @NotNull @Min(1) Integer minRentalDays,
        @NotNull @Min(1) Integer maxRentalDays,
        @NotNull Boolean smokingAllowed,
        @NotNull Boolean petsAllowed,
        @NotNull Boolean internationalTravelAllowed
) {
}
