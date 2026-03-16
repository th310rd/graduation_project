package com.p2p.listing.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ListingSummaryResponse(
        UUID id,
        UUID vehicleId,
        String title,
        String city,
        BigDecimal pricePerDay,
        boolean deliveryAvailable
) {
}
