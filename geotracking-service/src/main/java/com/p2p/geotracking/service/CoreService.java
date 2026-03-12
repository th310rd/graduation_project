package com.p2p.geotracking.service;

import com.p2p.geotracking.dto.CreateRequest;
import com.p2p.geotracking.dto.ResponseDto;
import com.p2p.geotracking.entity.VehicleLocation;
import com.p2p.geotracking.exception.NotFoundException;
import com.p2p.geotracking.repository.VehicleLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoreService {
    private final VehicleLocationRepository repository;

    public ResponseDto create(CreateRequest request) {
        VehicleLocation e = VehicleLocation.builder().externalRef(request.externalRef()).status("CREATED").build();
        return map(repository.save(e));
    }

    public ResponseDto get(UUID id) {
        return map(repository.findById(id).orElseThrow(() -> new NotFoundException("Resource not found")));
    }

    private ResponseDto map(VehicleLocation e) {
        return new ResponseDto(e.getId(), e.getExternalRef(), e.getStatus(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
