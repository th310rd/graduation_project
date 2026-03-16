package com.p2p.listing;

import com.p2p.listing.client.VehicleClient;
import com.p2p.listing.dto.CreateListingRequest;
import com.p2p.listing.entity.Listing;
import com.p2p.listing.events.ListingEventPublisher;
import com.p2p.listing.mapper.ListingMapper;
import com.p2p.listing.repository.ListingPhotoRepository;
import com.p2p.listing.repository.ListingRepository;
import com.p2p.listing.repository.ListingRulesRepository;
import com.p2p.listing.service.ListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceUnitTest {

    @Mock private ListingRepository listingRepository;
    @Mock private ListingPhotoRepository listingPhotoRepository;
    @Mock private ListingRulesRepository listingRulesRepository;
    @Mock private ListingMapper listingMapper;
    @Mock private VehicleClient vehicleClient;
    @Mock private ListingEventPublisher eventPublisher;

    @InjectMocks
    private ListingService listingService;

    private UUID actorId;

    @BeforeEach
    void setUp() {
        actorId = UUID.randomUUID();
    }

    @Test
    void createShouldValidateVehicleAndPublishEvent() {
        UUID vehicleId = UUID.randomUUID();
        when(vehicleClient.getVehicle(vehicleId)).thenReturn(new VehicleClient.VehicleResponse(vehicleId, actorId, "AVAILABLE"));

        Listing saved = Listing.builder()
                .id(UUID.randomUUID())
                .vehicleId(vehicleId)
                .ownerId(actorId)
                .title("BMW M5")
                .description("desc")
                .city("Tashkent")
                .pricePerDay(BigDecimal.valueOf(100))
                .depositAmount(BigDecimal.valueOf(100))
                .instantBooking(true)
                .deliveryAvailable(true)
                .build();
        when(listingRepository.save(any(Listing.class))).thenReturn(saved);
        when(listingRepository.findByIdAndStatusNot(eq(saved.getId()), any())).thenReturn(java.util.Optional.of(saved));
        when(listingPhotoRepository.findByListingIdOrderBySortOrderAsc(saved.getId())).thenReturn(java.util.List.of());
        when(listingRulesRepository.findByListingId(saved.getId())).thenReturn(java.util.Optional.empty());

        listingService.create(actorId, new CreateListingRequest(vehicleId, "BMW M5", "desc", "Tashkent", BigDecimal.valueOf(100), BigDecimal.valueOf(100), true, true));

        verify(vehicleClient).getVehicle(vehicleId);
        verify(eventPublisher, atLeastOnce()).publish(any(), any(), any());
        verify(listingRulesRepository).save(any());
    }
}
