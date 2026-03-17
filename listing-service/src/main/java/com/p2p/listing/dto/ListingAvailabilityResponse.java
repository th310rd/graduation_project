package com.p2p.listing.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ListingAvailabilityResponse(UUID listingId, UUID vehicleId, BigDecimal pricePerDay, boolean available) {
}
