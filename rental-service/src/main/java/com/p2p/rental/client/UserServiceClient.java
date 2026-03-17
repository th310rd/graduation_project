package com.p2p.rental.client;

import com.p2p.rental.dto.EligibilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserServiceClient {
    private final WebClient webClient;

    @Value("${clients.user-service.base-url}")
    private String baseUrl;

    public EligibilityResponse checkEligibility(UUID userId) {
        return webClient.get()
                .uri(baseUrl + "/users/" + userId + "/eligibility")
                .retrieve()
                .bodyToMono(EligibilityResponse.class)
                .block();
    }
}
