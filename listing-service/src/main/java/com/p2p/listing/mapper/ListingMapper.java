package com.p2p.listing.mapper;

import com.p2p.listing.dto.*;
import com.p2p.listing.entity.Listing;
import com.p2p.listing.entity.ListingPhoto;
import com.p2p.listing.entity.ListingRules;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListingMapper {
    public ListingResponse toResponse(Listing listing, List<ListingPhoto> photos, ListingRules rules) {
        return new ListingResponse(
                listing.getId(),
                listing.getVehicleId(),
                listing.getOwnerId(),
                listing.getTitle(),
                listing.getDescription(),
                listing.getCity(),
                listing.getPricePerDay(),
                listing.getDepositAmount(),
                listing.isInstantBooking(),
                listing.isDeliveryAvailable(),
                listing.getStatus(),
                photos.stream().map(this::toPhotoResponse).toList(),
                rules == null ? null : new ListingRulesResponse(
                        rules.getMinDriverAge(),
                        rules.getMinRentalDays(),
                        rules.getMaxRentalDays(),
                        rules.isSmokingAllowed(),
                        rules.isPetsAllowed(),
                        rules.isInternationalTravelAllowed()),
                listing.getCreatedAt(),
                listing.getUpdatedAt()
        );
    }

    public ListingPhotoResponse toPhotoResponse(ListingPhoto photo) {
        return new ListingPhotoResponse(photo.getId(), photo.getPhotoUrl(), photo.getSortOrder());
    }

    public ListingSummaryResponse toSummary(Listing listing) {
        return new ListingSummaryResponse(
                listing.getId(),
                listing.getVehicleId(),
                listing.getTitle(),
                listing.getCity(),
                listing.getPricePerDay(),
                listing.isDeliveryAvailable()
        );
    }
}
