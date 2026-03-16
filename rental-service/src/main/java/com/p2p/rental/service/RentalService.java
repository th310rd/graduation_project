package com.p2p.rental.service;

import com.p2p.rental.client.UserServiceClient;
import com.p2p.rental.client.VehicleServiceClient;
import com.p2p.rental.dto.*;
import com.p2p.rental.entity.Rental;
import com.p2p.rental.entity.RentalStatus;
import com.p2p.rental.exception.BadRequestException;
import com.p2p.rental.exception.NotFoundException;
import com.p2p.rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final UserServiceClient userServiceClient;
    private final VehicleServiceClient vehicleServiceClient;
    private final RentalEventPublisher eventPublisher;

    @Transactional
    public RentalResponse create(CreateRentalRequest request) {
        if (!request.endDateTime().isAfter(request.startDateTime())) {
            throw new BadRequestException("endDateTime must be after startDateTime");
        }

        boolean overlaps = rentalRepository.existsOverlapping(
                request.vehicleId(),
                request.startDateTime(),
                request.endDateTime(),
                List.of(RentalStatus.CREATED, RentalStatus.CONFIRMED, RentalStatus.ACTIVE)
        );
        if (overlaps) throw new BadRequestException("Overlapping rental exists for this vehicle");

        EligibilityResponse eligibility = userServiceClient.checkEligibility(request.guestId());
        if (eligibility == null || !eligibility.eligible()) {
            throw new BadRequestException("Guest is not eligible: " + (eligibility == null ? "unknown" : String.join(", ", eligibility.reasons())));
        }

        AvailabilityResponse availability = vehicleServiceClient.checkAvailability(request.vehicleId());
        if (availability == null || !availability.available()) {
            throw new BadRequestException("Vehicle is not available: " + (availability == null ? "unknown" : String.join(", ", availability.reasons())));
        }

        Rental rental = Rental.builder()
                .vehicleId(request.vehicleId())
                .guestId(request.guestId())
                .startDateTime(request.startDateTime())
                .endDateTime(request.endDateTime())
                .status(RentalStatus.CREATED)
                .build();
        Rental saved = rentalRepository.save(rental);
        eventPublisher.publish(com.p2p.rental.config.KafkaTopics.RENTAL_CREATED, saved.getId().toString(), Map.of("rentalId", saved.getId().toString(), "status", saved.getStatus().name(), "occurredAt", Instant.now().toString()));
        return map(saved);

        return map(rentalRepository.save(rental));
    }

    @Transactional
    public RentalResponse confirm(UUID id) {
        Rental rental = find(id);
        if (rental.getStatus() != RentalStatus.CREATED) {
            throw new BadRequestException("Only CREATED rental can be confirmed");
        }
        vehicleServiceClient.lock(rental.getVehicleId(), rental.getId());
        rental.setStatus(RentalStatus.CONFIRMED);
        Rental saved = rentalRepository.save(rental);
        eventPublisher.publish(com.p2p.rental.config.KafkaTopics.RENTAL_CONFIRMED, saved.getId().toString(), Map.of("rentalId", saved.getId().toString(), "status", saved.getStatus().name(), "occurredAt", Instant.now().toString()));
        return map(saved);

        return map(rentalRepository.save(rental));
    }

    @Transactional
    public RentalResponse activate(UUID id, ActivateRentalRequest request) {
        Rental rental = find(id);
        if (rental.getStatus() != RentalStatus.CONFIRMED) {
            throw new BadRequestException("Only CONFIRMED rental can be activated");
        }
        rental.setPickupMileage(request.pickupMileage());
        rental.setStatus(RentalStatus.ACTIVE);
        Rental saved = rentalRepository.save(rental);
        eventPublisher.publish(com.p2p.rental.config.KafkaTopics.RENTAL_ACTIVE, saved.getId().toString(), Map.of("rentalId", saved.getId().toString(), "status", saved.getStatus().name(), "occurredAt", Instant.now().toString()));
        return map(saved);

        return map(rentalRepository.save(rental));
    }

    @Transactional
    public RentalResponse complete(UUID id, CompleteRentalRequest request) {
        Rental rental = find(id);
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new BadRequestException("Only ACTIVE rental can be completed");
        }
        if (rental.getPickupMileage() != null && request.returnMileage() < rental.getPickupMileage()) {
            throw new BadRequestException("Return mileage cannot be less than pickup mileage");
        }
        rental.setReturnMileage(request.returnMileage());
        rental.setStatus(RentalStatus.COMPLETED);
        Rental saved = rentalRepository.save(rental);
        vehicleServiceClient.unlock(saved.getVehicleId(), saved.getId());
        eventPublisher.publish(com.p2p.rental.config.KafkaTopics.RENTAL_COMPLETED, saved.getId().toString(), Map.of("rentalId", saved.getId().toString(), "status", saved.getStatus().name(), "occurredAt", Instant.now().toString()));

        return map(saved);
    }

    public RentalResponse get(UUID id) {
        return map(find(id));
    }

    private Rental find(UUID id) {
        return rentalRepository.findById(id).orElseThrow(() -> new NotFoundException("Rental not found"));
    }

    private RentalResponse map(Rental r) {
        return new RentalResponse(r.getId(), r.getVehicleId(), r.getGuestId(), r.getStartDateTime(), r.getEndDateTime(),
                r.getPickupMileage(), r.getReturnMileage(), r.getStatus(), r.getCreatedAt(), r.getUpdatedAt());
    }
}
