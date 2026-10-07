package com.sailaja.aiserviceplatform.controller;

import com.sailaja.aiserviceplatform.kafka.ServiceRequestEventProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class KafkaTestController {

    private final ServiceRequestEventProducer producer;

    public KafkaTestController(ServiceRequestEventProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/test")
    public ResponseEntity<String> sendTestMessage(
            @RequestParam String message) {

        producer.sendMessage(message);

        return ResponseEntity.ok("Message sent to Kafka");
    }
}