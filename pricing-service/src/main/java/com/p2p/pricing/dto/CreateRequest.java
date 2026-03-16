package com.p2p.pricing.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRequest(@NotBlank String externalRef) {}
