package com.p2p.listing.dto;

import com.p2p.listing.entity.ListingStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ListingResponse(
        UUID id,
        UUID vehicleId,
        UUID ownerId,
        String title,
        String description,
        String city,
        BigDecimal pricePerDay,
        BigDecimal depositAmount,
        boolean instantBooking,
        boolean deliveryAvailable,
        ListingStatus status,
        List<ListingPhotoResponse> photos,
        ListingRulesResponse rules,
        Instant createdAt,
        Instant updatedAt
) {
}
