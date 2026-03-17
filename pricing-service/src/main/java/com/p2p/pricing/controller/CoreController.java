package com.p2p.pricing.controller;

import com.p2p.pricing.dto.CalculatePricingRequest;
import com.p2p.pricing.dto.PricingResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pricing")
public class CoreController {
    @PostMapping("/calculate")
    public PricingResponse calculate(@Valid @RequestBody CalculatePricingRequest request) {
        long base = request.basePrice();
        long fee = Math.round(base * 0.15);
        long deposit = Math.max(1_500_000L, Math.round(base * 1.2));
        long delivery = request.deliveryFee() == null ? 0 : request.deliveryFee();
        long penalties = request.penalties() == null ? 0 : request.penalties();
        long total = base + fee + delivery + penalties;
        return new PricingResponse(base, fee, deposit, delivery, total);
    }
}
