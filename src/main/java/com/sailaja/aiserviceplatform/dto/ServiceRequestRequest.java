package com.sailaja.aiserviceplatform.dto;

import com.sailaja.aiserviceplatform.enums.Priority;
import com.sailaja.aiserviceplatform.enums.ServiceRequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServiceRequestRequest (

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Priority is required")
        Priority priority,

        @NotNull(message = "Status is required")
        ServiceRequestStatus status
) {
}
