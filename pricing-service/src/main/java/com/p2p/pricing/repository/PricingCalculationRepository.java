package com.p2p.pricing.repository;

import com.p2p.pricing.entity.PricingCalculation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PricingCalculationRepository extends JpaRepository<PricingCalculation, UUID> {}
