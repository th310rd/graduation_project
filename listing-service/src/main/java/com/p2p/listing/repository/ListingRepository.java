package com.p2p.listing.repository;

import com.p2p.listing.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListingRepository extends JpaRepository<Listing, UUID> {}
