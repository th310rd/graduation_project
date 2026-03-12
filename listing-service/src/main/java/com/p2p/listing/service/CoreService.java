package com.p2p.listing.service;

import com.p2p.listing.dto.CreateRequest;
import com.p2p.listing.dto.ResponseDto;
import com.p2p.listing.entity.Listing;
import com.p2p.listing.exception.NotFoundException;
import com.p2p.listing.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoreService {
    private final ListingRepository repository;

    public ResponseDto create(CreateRequest request) {
        Listing e = Listing.builder().externalRef(request.externalRef()).status("CREATED").build();
        return map(repository.save(e));
    }

    public ResponseDto get(UUID id) {
        return map(repository.findById(id).orElseThrow(() -> new NotFoundException("Resource not found")));
    }

    private ResponseDto map(Listing e) {
        return new ResponseDto(e.getId(), e.getExternalRef(), e.getStatus(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
