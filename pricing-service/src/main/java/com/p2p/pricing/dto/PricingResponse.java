package com.p2p.pricing.dto;

public record PricingResponse(long basePrice, long platformFee, long deposit, long deliveryFee, long total) {}
