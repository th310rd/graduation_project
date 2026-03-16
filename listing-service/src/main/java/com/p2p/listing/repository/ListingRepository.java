package com.p2p.listing.repository;

import com.p2p.listing.entity.Listing;
import com.p2p.listing.entity.ListingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ListingRepository extends JpaRepository<Listing, UUID>, JpaSpecificationExecutor<Listing> {
    Optional<Listing> findByIdAndStatusNot(UUID id, ListingStatus status);
}
