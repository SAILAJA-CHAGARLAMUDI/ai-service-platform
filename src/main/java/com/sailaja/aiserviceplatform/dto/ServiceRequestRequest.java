package com.sailaja.aiserviceplatform.dto;

import jakarta.validation.constraints.NotBlank;

public record ServiceRequestRequest (

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Priority is required")
        String priority,

        @NotBlank(message = "Status is required")
        String status
) {
}
