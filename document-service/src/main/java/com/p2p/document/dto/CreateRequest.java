package com.p2p.document.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRequest(@NotBlank String externalRef) {}
