package com.p2p.listing.service;

import com.p2p.listing.client.VehicleClient;
import com.p2p.listing.config.KafkaTopics;
import com.p2p.listing.dto.*;
import com.p2p.listing.entity.Listing;
import com.p2p.listing.entity.ListingPhoto;
import com.p2p.listing.entity.ListingRules;
import com.p2p.listing.entity.ListingStatus;
import com.p2p.listing.events.ListingEventPublisher;
import com.p2p.listing.exception.BadRequestException;
import com.p2p.listing.exception.ForbiddenException;
import com.p2p.listing.exception.NotFoundException;
import com.p2p.listing.mapper.ListingMapper;
import com.p2p.listing.repository.ListingPhotoRepository;
import com.p2p.listing.repository.ListingRepository;
import com.p2p.listing.repository.ListingRulesRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ListingService {
    private final ListingRepository listingRepository;
    private final ListingPhotoRepository listingPhotoRepository;
    private final ListingRulesRepository listingRulesRepository;
    private final ListingMapper listingMapper;
    private final VehicleClient vehicleClient;
    private final ListingEventPublisher eventPublisher;

    @Transactional
    public ListingResponse create(UUID actorId, CreateListingRequest request) {
        VehicleClient.VehicleResponse vehicle = vehicleClient.getVehicle(request.vehicleId());
        if (!vehicle.ownerId().equals(actorId)) {
            throw new ForbiddenException("Only vehicle owner can create listing");
        }

        Listing listing = Listing.builder()
                .vehicleId(request.vehicleId())
                .ownerId(actorId)
                .title(request.title())
                .description(request.description())
                .city(request.city())
                .pricePerDay(request.pricePerDay())
                .depositAmount(request.depositAmount())
                .instantBooking(Boolean.TRUE.equals(request.instantBooking()))
                .deliveryAvailable(Boolean.TRUE.equals(request.deliveryAvailable()))
                .status(ListingStatus.ACTIVE)
                .build();

        Listing saved = listingRepository.save(listing);
        saveDefaultRules(saved.getId());
        publishEvent(KafkaTopics.LISTING_CREATED, saved);

        return get(saved.getId());
    }

    @Transactional(readOnly = true)
    public ListingResponse get(UUID listingId) {
        Listing listing = findActive(listingId);
        List<ListingPhoto> photos = listingPhotoRepository.findByListingIdOrderBySortOrderAsc(listingId);
        ListingRules rules = listingRulesRepository.findByListingId(listingId).orElse(null);
        return listingMapper.toResponse(listing, photos, rules);
    }

    @Transactional
    public ListingResponse update(UUID actorId, UUID listingId, UpdateListingRequest request) {
        Listing listing = findActive(listingId);
        validateOwner(actorId, listing);

        listing.setTitle(request.title());
        listing.setDescription(request.description());
        listing.setCity(request.city());
        listing.setPricePerDay(request.pricePerDay());
        listing.setDepositAmount(request.depositAmount());
        listing.setInstantBooking(Boolean.TRUE.equals(request.instantBooking()));
        listing.setDeliveryAvailable(Boolean.TRUE.equals(request.deliveryAvailable()));

        Listing saved = listingRepository.save(listing);
        publishEvent(KafkaTopics.LISTING_UPDATED, saved);
        return get(saved.getId());
    }

    @Transactional
    public void delete(UUID actorId, UUID listingId) {
        Listing listing = findActive(listingId);
        validateOwner(actorId, listing);
        listing.setStatus(ListingStatus.DELETED);
        Listing saved = listingRepository.save(listing);
        publishEvent(KafkaTopics.LISTING_DELETED, saved);
    }

    @Transactional(readOnly = true)
    public List<ListingSummaryResponse> search(String city, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice,
                                               Boolean deliveryAvailable) {
        Specification<Listing> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), ListingStatus.ACTIVE));
            if (city != null && !city.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerDay"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerDay"), maxPrice));
            }
            if (deliveryAvailable != null) {
                predicates.add(cb.equal(root.get("deliveryAvailable"), deliveryAvailable));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return listingRepository.findAll(specification).stream().map(listingMapper::toSummary).toList();
    }

    @Transactional
    public ListingPhotoResponse addPhoto(UUID actorId, UUID listingId, ListingPhotoRequest request) {
        Listing listing = findActive(listingId);
        validateOwner(actorId, listing);

        ListingPhoto photo = ListingPhoto.builder()
                .listingId(listingId)
                .photoUrl(request.photoUrl())
                .sortOrder(request.sortOrder())
                .build();
        return listingMapper.toPhotoResponse(listingPhotoRepository.save(photo));
    }

    @Transactional
    public ListingRulesResponse updateRules(UUID actorId, UUID listingId, ListingRulesRequest request) {
        if (request.maxRentalDays() < request.minRentalDays()) {
            throw new BadRequestException("maxRentalDays must be >= minRentalDays");
        }

        Listing listing = findActive(listingId);
        validateOwner(actorId, listing);

        ListingRules rules = listingRulesRepository.findByListingId(listingId).orElse(ListingRules.builder().listingId(listingId).build());
        rules.setMinDriverAge(request.minDriverAge());
        rules.setMinRentalDays(request.minRentalDays());
        rules.setMaxRentalDays(request.maxRentalDays());
        rules.setSmokingAllowed(request.smokingAllowed());
        rules.setPetsAllowed(request.petsAllowed());
        rules.setInternationalTravelAllowed(request.internationalTravelAllowed());
        ListingRules saved = listingRulesRepository.save(rules);

        return new ListingRulesResponse(saved.getMinDriverAge(), saved.getMinRentalDays(), saved.getMaxRentalDays(),
                saved.isSmokingAllowed(), saved.isPetsAllowed(), saved.isInternationalTravelAllowed());
    }

    @Transactional(readOnly = true)
    public ListingAvailabilityResponse availability(UUID listingId) {
        Listing listing = findActive(listingId);
        VehicleClient.VehicleResponse vehicle = vehicleClient.getVehicle(listing.getVehicleId());
        boolean available = "AVAILABLE".equalsIgnoreCase(vehicle.status());
        return new ListingAvailabilityResponse(listing.getId(), listing.getVehicleId(), listing.getPricePerDay(), available);
    }

    private void saveDefaultRules(UUID listingId) {
        ListingRules rules = ListingRules.builder()
                .listingId(listingId)
                .minDriverAge(21)
                .minRentalDays(1)
                .maxRentalDays(30)
                .smokingAllowed(false)
                .petsAllowed(false)
                .internationalTravelAllowed(false)
                .build();
        listingRulesRepository.save(rules);
    }

    private Listing findActive(UUID listingId) {
        return listingRepository.findByIdAndStatusNot(listingId, ListingStatus.DELETED)
                .orElseThrow(() -> new NotFoundException("Listing not found"));
    }

    private void validateOwner(UUID actorId, Listing listing) {
        if (!listing.getOwnerId().equals(actorId)) {
            throw new ForbiddenException("Only listing owner can modify listing");
        }
    }

    private void publishEvent(String topic, Listing listing) {
        eventPublisher.publish(topic, listing.getId().toString(), Map.of(
                "listingId", listing.getId().toString(),
                "vehicleId", listing.getVehicleId().toString(),
                "ownerId", listing.getOwnerId().toString(),
                "status", listing.getStatus().name(),
                "occurredAt", Instant.now().toString()
        ));
    }
}
