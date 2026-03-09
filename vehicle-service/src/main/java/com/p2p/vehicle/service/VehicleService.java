package com.p2p.vehicle.service;

import com.p2p.vehicle.dto.*;
import com.p2p.vehicle.entity.Vehicle;
import com.p2p.vehicle.entity.VehicleStatus;
import com.p2p.vehicle.exception.BadRequestException;
import com.p2p.vehicle.exception.NotFoundException;
import com.p2p.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleResponse register(CreateVehicleRequest request) {
        Vehicle vehicle = Vehicle.builder()
                .ownerId(request.ownerId())
                .vin(request.vin())
                .licensePlate(request.licensePlate())
                .brand(request.brand())
                .model(request.model())
                .year(request.year())
                .color(request.color())
                .mileage(request.mileage())
                .insuranceExpiry(request.insuranceExpiry())
                .technicalInspectionExpiry(request.technicalInspectionExpiry())
                .status(request.status() == null ? VehicleStatus.AVAILABLE : request.status())
                .build();
        return map(vehicleRepository.save(vehicle));
    }

    public VehicleResponse get(UUID id) {
        return map(find(id));
    }

    public AvailabilityResponse availability(UUID id) {
        Vehicle vehicle = find(id);
        List<String> reasons = new ArrayList<>();
        LocalDate today = LocalDate.now();
        if (!vehicle.getInsuranceExpiry().isAfter(today)) reasons.add("Insurance expired");
        if (!vehicle.getTechnicalInspectionExpiry().isAfter(today)) reasons.add("Technical inspection expired");
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) reasons.add("Vehicle status is not AVAILABLE");
        return new AvailabilityResponse(reasons.isEmpty(), reasons);
    }

    public VehicleResponse lock(UUID id, LockRequest request) {
        Vehicle vehicle = find(id);
        AvailabilityResponse availability = availability(id);
        if (!availability.available()) throw new BadRequestException(String.join(", ", availability.reasons()));
        vehicle.setStatus(VehicleStatus.RENTED);
        vehicle.setCurrentRentalId(request.rentalId());
        return map(vehicleRepository.save(vehicle));
    }

    public VehicleResponse unlock(UUID id, LockRequest request) {
        Vehicle vehicle = find(id);
        if (vehicle.getCurrentRentalId() != null && !vehicle.getCurrentRentalId().equals(request.rentalId())) {
            throw new BadRequestException("Vehicle locked by different rental");
        }
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicle.setCurrentRentalId(null);
        return map(vehicleRepository.save(vehicle));
    }

    private Vehicle find(UUID id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new NotFoundException("Vehicle not found"));
    }

    private VehicleResponse map(Vehicle v) {
        return new VehicleResponse(v.getId(), v.getOwnerId(), v.getVin(), v.getLicensePlate(), v.getBrand(), v.getModel(),
                v.getYear(), v.getColor(), v.getMileage(), v.getInsuranceExpiry(), v.getTechnicalInspectionExpiry(),
                v.getStatus(), v.getCurrentRentalId(), v.getCreatedAt(), v.getUpdatedAt());
    }
}
