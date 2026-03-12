package com.p2p.signature.service;

import com.p2p.signature.dto.CreateRequest;
import com.p2p.signature.dto.ResponseDto;
import com.p2p.signature.entity.Contract;
import com.p2p.signature.exception.NotFoundException;
import com.p2p.signature.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoreService {
    private final ContractRepository repository;

    public ResponseDto create(CreateRequest request) {
        Contract e = Contract.builder().externalRef(request.externalRef()).status("CREATED").build();
        return map(repository.save(e));
    }

    public ResponseDto get(UUID id) {
        return map(repository.findById(id).orElseThrow(() -> new NotFoundException("Resource not found")));
    }

    private ResponseDto map(Contract e) {
        return new ResponseDto(e.getId(), e.getExternalRef(), e.getStatus(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
