package com.p2p.notification.dto;

import java.time.Instant;
import java.util.UUID;

public record ResponseDto(UUID id, String externalRef, String status, Instant createdAt, Instant updatedAt) {}
