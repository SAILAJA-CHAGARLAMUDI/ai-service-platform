package com.sailaja.aiserviceplatform.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class ServiceRequestEventConsumer {

    private final ObjectMapper objectMapper;

    public ServiceRequestEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "service-request-events",
            groupId = "ai-service-platform"
    )
    public void consumeMessage(String message) {

        try {
            ServiceRequestEvent event =
                    objectMapper.readValue(
                            message,
                            ServiceRequestEvent.class
                    );

            System.out.println(
                    "Received event: " + event
            );

        } catch (Exception exception) {
            System.err.println(
                    "Failed to process Kafka event: "
                            + exception.getMessage()
            );
        }
    }
}