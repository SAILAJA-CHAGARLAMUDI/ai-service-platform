package com.sailaja.aiserviceplatform.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class ServiceRequestEventProducer {

    private static final String TOPIC = "service-request-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ServiceRequestEventProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendEvent(ServiceRequestEvent event) {

        String message = objectMapper.writeValueAsString(event);

        kafkaTemplate.send(TOPIC, message);
    }
}