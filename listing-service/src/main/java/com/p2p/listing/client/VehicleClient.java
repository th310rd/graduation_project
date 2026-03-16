package com.p2p.listing.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "vehicleClient", url = "${clients.vehicle-service.base-url}")
public interface VehicleClient {
    @GetMapping("/vehicles/{id}")
    VehicleResponse getVehicle(@PathVariable("id") UUID id);

    record VehicleResponse(UUID id, UUID ownerId, String status) {
    }
}
