package com.sailaja.aiserviceplatform.dto;

import jakarta.validation.constraints.NotBlank;

public record ServiceRequestResponse (
        Long id,
        String title,
        String description,
        String priority,
        String status
){
}
