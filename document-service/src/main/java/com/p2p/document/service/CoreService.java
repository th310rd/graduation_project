package com.p2p.document.service;

import com.p2p.document.dto.CreateRequest;
import com.p2p.document.dto.ResponseDto;
import com.p2p.document.entity.DocumentRecord;
import com.p2p.document.exception.NotFoundException;
import com.p2p.document.repository.DocumentRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoreService {
    private final DocumentRecordRepository repository;

    public ResponseDto create(CreateRequest request) {
        DocumentRecord e = DocumentRecord.builder().externalRef(request.externalRef()).status("CREATED").build();
        return map(repository.save(e));
    }

    public ResponseDto get(UUID id) {
        return map(repository.findById(id).orElseThrow(() -> new NotFoundException("Resource not found")));
    }

    private ResponseDto map(DocumentRecord e) {
        return new ResponseDto(e.getId(), e.getExternalRef(), e.getStatus(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
