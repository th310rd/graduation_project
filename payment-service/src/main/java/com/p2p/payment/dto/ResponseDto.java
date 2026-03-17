package com.p2p.payment.dto;

import java.time.Instant;
import java.util.UUID;

public record ResponseDto(UUID id, String externalRef, String status, Instant createdAt, Instant updatedAt) {}
