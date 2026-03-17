package com.p2p.vehicle.dto;

import com.p2p.vehicle.entity.VehicleStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record VehicleResponse(
        UUID id, UUID ownerId, String vin, String licensePlate, String brand, String model,
        Integer year, String color, Long mileage, LocalDate insuranceExpiry, LocalDate technicalInspectionExpiry,
        VehicleStatus status, UUID currentRentalId, Instant createdAt, Instant updatedAt
) {}
