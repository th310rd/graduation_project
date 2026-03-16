package com.p2p.rental.config;

public final class KafkaTopics {
    public static final String RENTAL_CREATED = "rental.created";
    public static final String RENTAL_CONFIRMED = "rental.confirmed";
    public static final String RENTAL_ACTIVE = "rental.active";
    public static final String RENTAL_COMPLETED = "rental.completed";
    private KafkaTopics() {}
}
