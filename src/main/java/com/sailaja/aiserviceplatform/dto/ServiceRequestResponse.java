package com.sailaja.aiserviceplatform.dto;

import com.sailaja.aiserviceplatform.enums.Priority;
import com.sailaja.aiserviceplatform.enums.ServiceRequestStatus;

public record ServiceRequestResponse (
        Long id,
        String title,
        String description,
        Priority priority,
        ServiceRequestStatus status
) {
}
