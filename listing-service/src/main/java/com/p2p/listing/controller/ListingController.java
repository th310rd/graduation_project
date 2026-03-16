package com.p2p.listing.controller;

import com.p2p.listing.dto.*;
import com.p2p.listing.service.AuthContextService;
import com.p2p.listing.service.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/listings")
public class ListingController {
    private final ListingService listingService;
    private final AuthContextService authContextService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ListingResponse create(@RequestHeader("Authorization") String authorization,
                                  @Valid @RequestBody CreateListingRequest request) {
        UUID actorId = authContextService.extractUserIdFromAuthorization(authorization);
        return listingService.create(actorId, request);
    }

    @GetMapping("/{id}")
    public ListingResponse get(@PathVariable UUID id) {
        return listingService.get(id);
    }

    @PutMapping("/{id}")
    public ListingResponse update(@RequestHeader("Authorization") String authorization,
                                  @PathVariable UUID id,
                                  @Valid @RequestBody UpdateListingRequest request) {
        UUID actorId = authContextService.extractUserIdFromAuthorization(authorization);
        return listingService.update(actorId, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader("Authorization") String authorization, @PathVariable UUID id) {
        UUID actorId = authContextService.extractUserIdFromAuthorization(authorization);
        listingService.delete(actorId, id);
    }

    @GetMapping("/search")
    public List<ListingSummaryResponse> search(@RequestParam(required = false) String city,
                                               @RequestParam(required = false) BigDecimal minPrice,
                                               @RequestParam(required = false) BigDecimal maxPrice,
                                               @RequestParam(required = false) Boolean deliveryAvailable) {
        return listingService.search(city, minPrice, maxPrice, deliveryAvailable);
    }

    @PostMapping("/{listingId}/photos")
    @ResponseStatus(HttpStatus.CREATED)
    public ListingPhotoResponse addPhoto(@RequestHeader("Authorization") String authorization,
                                         @PathVariable UUID listingId,
                                         @Valid @RequestBody ListingPhotoRequest request) {
        UUID actorId = authContextService.extractUserIdFromAuthorization(authorization);
        return listingService.addPhoto(actorId, listingId, request);
    }

    @PutMapping("/{listingId}/rules")
    public ListingRulesResponse updateRules(@RequestHeader("Authorization") String authorization,
                                            @PathVariable UUID listingId,
                                            @Valid @RequestBody ListingRulesRequest request) {
        UUID actorId = authContextService.extractUserIdFromAuthorization(authorization);
        return listingService.updateRules(actorId, listingId, request);
    }

    @GetMapping("/{listingId}/availability")
    public ListingAvailabilityResponse availability(@PathVariable UUID listingId) {
        return listingService.availability(listingId);
    }
}
