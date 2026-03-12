package com.p2p.listing.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRequest(@NotBlank String externalRef) {}
