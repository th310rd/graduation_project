package com.p2p.vehicle.dto;

import com.p2p.vehicle.entity.VehicleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateVehicleRequest(
        @NotNull UUID ownerId,
        @NotBlank String vin,
        @NotBlank String licensePlate,
        @NotBlank String brand,
        @NotBlank String model,
        @NotNull Integer year,
        @NotBlank String color,
        @NotNull Long mileage,
        @NotNull LocalDate insuranceExpiry,
        @NotNull LocalDate technicalInspectionExpiry,
        VehicleStatus status
) {}
