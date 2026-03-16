package com.p2p.listing.repository;

import com.p2p.listing.entity.ListingPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ListingPhotoRepository extends JpaRepository<ListingPhoto, UUID> {
    List<ListingPhoto> findByListingIdOrderBySortOrderAsc(UUID listingId);
}
