package com.p2p.listing.dto;

import java.util.UUID;

public record ListingPhotoResponse(UUID id, String photoUrl, Integer sortOrder) {
}
