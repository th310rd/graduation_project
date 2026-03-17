package com.p2p.notification.service;

import com.p2p.notification.dto.CreateRequest;
import com.p2p.notification.dto.ResponseDto;
import com.p2p.notification.entity.NotificationRecord;
import com.p2p.notification.exception.NotFoundException;
import com.p2p.notification.repository.NotificationRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoreService {
    private final NotificationRecordRepository repository;

    public ResponseDto create(CreateRequest request) {
        NotificationRecord e = NotificationRecord.builder().externalRef(request.externalRef()).status("CREATED").build();
        return map(repository.save(e));
    }

    public ResponseDto get(UUID id) {
        return map(repository.findById(id).orElseThrow(() -> new NotFoundException("Resource not found")));
    }

    private ResponseDto map(NotificationRecord e) {
        return new ResponseDto(e.getId(), e.getExternalRef(), e.getStatus(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
