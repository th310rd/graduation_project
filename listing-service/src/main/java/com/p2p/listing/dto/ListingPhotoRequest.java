package com.p2p.listing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ListingPhotoRequest(@NotBlank String photoUrl, @NotNull Integer sortOrder) {
}
