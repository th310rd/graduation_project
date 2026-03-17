package com.p2p.pricing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pricing_calculations")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PricingCalculation {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String externalRef;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
