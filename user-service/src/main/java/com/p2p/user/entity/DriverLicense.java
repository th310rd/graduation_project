package com.p2p.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "driver_licenses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverLicense {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID userId;
    @Column(nullable = false, unique = true)
    private String licenseNumber;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private LocalDate issuedDate;
    @Column(nullable = false)
    private LocalDate expiryDate;
    @Column(nullable = false)
    private String issuedBy;
    @Column(nullable = false)
    private String countryCode;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }
}
