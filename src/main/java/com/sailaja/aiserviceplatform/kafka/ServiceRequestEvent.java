package com.sailaja.aiserviceplatform.kafka;

import java.time.LocalDateTime;

public record ServiceRequestEvent(
        String eventType,
        Long requestId,
        LocalDateTime timestamp,
        String source
) {
}