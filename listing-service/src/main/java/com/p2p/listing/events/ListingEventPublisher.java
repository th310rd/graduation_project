package com.p2p.listing.events;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ListingEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(String topic, String key, Map<String, Object> payload) {
        kafkaTemplate.send(topic, key, payload);
    }
}
