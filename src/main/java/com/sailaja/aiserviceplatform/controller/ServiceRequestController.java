package com.sailaja.aiserviceplatform.controller;

import com.sailaja.aiserviceplatform.dto.ServiceRequestRequest;
import com.sailaja.aiserviceplatform.dto.ServiceRequestResponse;
import com.sailaja.aiserviceplatform.entity.ServiceRequest;
import com.sailaja.aiserviceplatform.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-requests")
public class ServiceRequestController {

    private final ServiceRequestService service;

    public ServiceRequestController(ServiceRequestService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ServiceRequestResponse>> getAllRequests() {

        return ResponseEntity.ok(service.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> getRequestById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getRequestById(id));
    }

    @PostMapping
    public ResponseEntity<ServiceRequestResponse> createRequest(
            @Valid @RequestBody ServiceRequestRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createRequest(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> updateRequest(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequestRequest request) {

        return ResponseEntity.ok(
                service.updateRequest(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(
            @PathVariable Long id) {

        service.deleteRequest(id);

        return ResponseEntity.noContent().build();
    }
}
