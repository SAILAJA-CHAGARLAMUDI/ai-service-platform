package com.sailaja.aiserviceplatform.controller;

import com.sailaja.aiserviceplatform.kafka.ServiceRequestEvent;
import com.sailaja.aiserviceplatform.kafka.ServiceRequestEventProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/kafka")
public class KafkaTestController {

    private final ServiceRequestEventProducer producer;

    public KafkaTestController(ServiceRequestEventProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/test")
    public ResponseEntity<String> sendTestMessage(
            @RequestParam Long requestId) {

        ServiceRequestEvent event = new ServiceRequestEvent(
                "SERVICE_REQUEST_CREATED",
                requestId,
                LocalDateTime.now(),
                "service-request-service"
        );

        producer.sendEvent(event);

        return ResponseEntity.ok("Service request event sent to Kafka");
    }
}