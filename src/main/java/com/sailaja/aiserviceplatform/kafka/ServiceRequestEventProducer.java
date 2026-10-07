package com.sailaja.aiserviceplatform.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServiceRequestEventProducer {

    private static final String TOPIC = "service-request-events";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ServiceRequestEventProducer(
            KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}