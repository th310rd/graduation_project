package com.p2p.listing;

import com.p2p.listing.entity.Listing;
import com.p2p.listing.entity.ListingStatus;
import com.p2p.listing.repository.ListingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
class ListingRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("listingdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ListingRepository listingRepository;

    @Test
    void shouldPersistListing() {
        Listing listing = Listing.builder()
                .vehicleId(UUID.randomUUID())
                .ownerId(UUID.randomUUID())
                .title("BMW M5")
                .description("Excellent condition")
                .city("Tashkent")
                .pricePerDay(BigDecimal.valueOf(120))
                .depositAmount(BigDecimal.valueOf(500))
                .instantBooking(true)
                .deliveryAvailable(true)
                .status(ListingStatus.ACTIVE)
                .build();

        Listing saved = listingRepository.save(listing);

        assertThat(saved.getId()).isNotNull();
        assertThat(listingRepository.findById(saved.getId())).isPresent();
    }
}
