package com.p2p.listing.dto;

public record ListingRulesResponse(
        Integer minDriverAge,
        Integer minRentalDays,
        Integer maxRentalDays,
        boolean smokingAllowed,
        boolean petsAllowed,
        boolean internationalTravelAllowed
) {
}
