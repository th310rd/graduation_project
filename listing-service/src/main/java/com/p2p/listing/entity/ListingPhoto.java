package com.p2p.listing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "listing_photos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingPhoto {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID listingId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String photoUrl;

    @Column(nullable = false)
    private Integer sortOrder;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }
}
