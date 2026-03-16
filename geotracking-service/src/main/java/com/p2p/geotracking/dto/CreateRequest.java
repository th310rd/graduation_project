package com.p2p.geotracking.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRequest(@NotBlank String externalRef) {}
