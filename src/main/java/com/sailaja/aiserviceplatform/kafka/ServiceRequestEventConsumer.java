package com.sailaja.aiserviceplatform.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ServiceRequestEventConsumer {

    @KafkaListener(
            topics = "service-request-events",
            groupId = "ai-service-platform"
    )
    public void consumeMessage(String message) {

        System.out.println(
                "Received Kafka message: " + message
        );
    }
}