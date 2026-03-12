package com.p2p.vehicle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID ownerId;
    @Column(nullable = false, unique = true)
    private String vin;
    @Column(nullable = false, unique = true)
    private String licensePlate;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private Integer year;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private Long mileage;
    @Column(nullable = false)
    private LocalDate insuranceExpiry;
    @Column(nullable = false)
    private LocalDate technicalInspectionExpiry;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status;
    @Column(nullable = false)
    private UUID currentRentalId;
    @Column(nullable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (status == null) status = VehicleStatus.AVAILABLE;
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
