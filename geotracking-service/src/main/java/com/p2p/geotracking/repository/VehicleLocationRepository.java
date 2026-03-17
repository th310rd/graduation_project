package com.p2p.geotracking.repository;

import com.p2p.geotracking.entity.VehicleLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleLocationRepository extends JpaRepository<VehicleLocation, UUID> {}
