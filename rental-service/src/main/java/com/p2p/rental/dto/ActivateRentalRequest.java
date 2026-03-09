package com.p2p.rental.dto;

import jakarta.validation.constraints.NotNull;

public record ActivateRentalRequest(@NotNull Long pickupMileage) {}
