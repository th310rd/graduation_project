package com.p2p.payment.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRequest(@NotBlank String externalRef) {}
