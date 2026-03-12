package com.p2p.vehicle.controller;

import com.p2p.vehicle.dto.*;
import com.p2p.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse register(@Valid @RequestBody CreateVehicleRequest request) {
        return vehicleService.register(request);
    }

    @GetMapping("/{id}")
    public VehicleResponse get(@PathVariable UUID id) {
        return vehicleService.get(id);
    }

    @GetMapping("/{id}/availability")
    public AvailabilityResponse availability(@PathVariable UUID id) {
        return vehicleService.availability(id);
    }

    @PostMapping("/{id}/lock")
    public VehicleResponse lock(@PathVariable UUID id, @Valid @RequestBody LockRequest request) {
        return vehicleService.lock(id, request);
    }

    @PostMapping("/{id}/unlock")
    public VehicleResponse unlock(@PathVariable UUID id, @Valid @RequestBody LockRequest request) {
        return vehicleService.unlock(id, request);
    }
}
