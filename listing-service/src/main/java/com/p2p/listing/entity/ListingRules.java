package com.p2p.listing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "listing_rules")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingRules {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID listingId;

    @Column(nullable = false)
    private Integer minDriverAge;

    @Column(nullable = false)
    private Integer minRentalDays;

    @Column(nullable = false)
    private Integer maxRentalDays;

    @Column(nullable = false)
    private boolean smokingAllowed;

    @Column(nullable = false)
    private boolean petsAllowed;

    @Column(nullable = false)
    private boolean internationalTravelAllowed;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }
}
