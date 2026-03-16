package com.p2p.pricing.dto;

import jakarta.validation.constraints.NotNull;

public record CalculatePricingRequest(@NotNull Long basePrice, Long deliveryFee, Long penalties) {}
