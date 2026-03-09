package com.p2p.vehicle.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LockRequest(@NotNull UUID rentalId) {}
