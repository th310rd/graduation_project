package com.p2p.listing.config;

public final class KafkaTopics {
    public static final String LISTING_CREATED = "listing.created";
    public static final String LISTING_UPDATED = "listing.updated";
    public static final String LISTING_DELETED = "listing.deleted";

    private KafkaTopics() {
    }
}
