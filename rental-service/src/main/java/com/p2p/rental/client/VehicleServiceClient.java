package com.p2p.rental.client;

import com.p2p.rental.dto.AvailabilityResponse;
import com.p2p.rental.dto.LockRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VehicleServiceClient {
    private final WebClient webClient;

    @Value("${clients.vehicle-service.base-url}")
    private String baseUrl;

    public AvailabilityResponse checkAvailability(UUID vehicleId) {
        return webClient.get()
                .uri(baseUrl + "/vehicles/" + vehicleId + "/availability")
                .retrieve()
                .bodyToMono(AvailabilityResponse.class)
                .block();
    }

    public void lock(UUID vehicleId, UUID rentalId) {
        webClient.post()
                .uri(baseUrl + "/vehicles/" + vehicleId + "/lock")
                .bodyValue(new LockRequest(rentalId))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void unlock(UUID vehicleId, UUID rentalId) {
        webClient.post()
                .uri(baseUrl + "/vehicles/" + vehicleId + "/unlock")
                .bodyValue(new LockRequest(rentalId))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
