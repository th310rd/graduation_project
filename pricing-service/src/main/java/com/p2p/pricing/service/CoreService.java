package com.p2p.pricing.service;

import com.p2p.pricing.dto.CreateRequest;
import com.p2p.pricing.dto.ResponseDto;
import com.p2p.pricing.entity.PricingCalculation;
import com.p2p.pricing.exception.NotFoundException;
import com.p2p.pricing.repository.PricingCalculationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoreService {
    private final PricingCalculationRepository repository;

    public ResponseDto create(CreateRequest request) {
        PricingCalculation e = PricingCalculation.builder().externalRef(request.externalRef()).status("CREATED").build();
        return map(repository.save(e));
    }

    public ResponseDto get(UUID id) {
        return map(repository.findById(id).orElseThrow(() -> new NotFoundException("Resource not found")));
    }

    private ResponseDto map(PricingCalculation e) {
        return new ResponseDto(e.getId(), e.getExternalRef(), e.getStatus(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
