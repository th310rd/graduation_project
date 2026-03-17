package com.p2p.payment.service;

import com.p2p.payment.dto.CreateRequest;
import com.p2p.payment.dto.ResponseDto;
import com.p2p.payment.entity.Payment;
import com.p2p.payment.exception.NotFoundException;
import com.p2p.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoreService {
    private final PaymentRepository repository;

    public ResponseDto create(CreateRequest request) {
        Payment e = Payment.builder().externalRef(request.externalRef()).status("CREATED").build();
        return map(repository.save(e));
    }

    public ResponseDto get(UUID id) {
        return map(repository.findById(id).orElseThrow(() -> new NotFoundException("Resource not found")));
    }

    private ResponseDto map(Payment e) {
        return new ResponseDto(e.getId(), e.getExternalRef(), e.getStatus(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
