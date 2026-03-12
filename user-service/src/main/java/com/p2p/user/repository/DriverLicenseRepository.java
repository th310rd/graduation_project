package com.p2p.user.repository;

import com.p2p.user.entity.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DriverLicenseRepository extends JpaRepository<DriverLicense, UUID> {
    Optional<DriverLicense> findByUserId(UUID userId);
}
