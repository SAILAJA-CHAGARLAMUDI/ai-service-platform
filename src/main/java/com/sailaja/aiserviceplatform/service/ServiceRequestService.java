package com.sailaja.aiserviceplatform.service;

import com.sailaja.aiserviceplatform.dto.ServiceRequestRequest;
import com.sailaja.aiserviceplatform.dto.ServiceRequestResponse;
import com.sailaja.aiserviceplatform.entity.ServiceRequest;
import com.sailaja.aiserviceplatform.repository.ServiceRequestRepository;
import org.springframework.stereotype.Service;
import com.sailaja.aiserviceplatform.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository repository;

    public ServiceRequestService(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    public List<ServiceRequestResponse> getAllRequests() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ServiceRequestResponse getRequestById(Long id) {
        ServiceRequest request = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Service request not found with id: " + id));

        return toResponse(request);
    }

    public ServiceRequestResponse  createRequest(ServiceRequestRequest request) {
        ServiceRequest entity = new ServiceRequest();

        entity.setTitle(request.title());
        entity.setDescription(request.description());
        entity.setPriority(request.priority());
        entity.setStatus(request.status());

        ServiceRequest savedRequest = repository.save(entity);

        return toResponse(savedRequest);
    }

    public ServiceRequestResponse updateRequest(
            Long id,
            ServiceRequestRequest request) {

        ServiceRequest entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Service request not found with id: " + id));

        entity.setTitle(request.title());
        entity.setDescription(request.description());
        entity.setPriority(request.priority());
        entity.setStatus(request.status());

        ServiceRequest updatedRequest = repository.save(entity);

        return toResponse(updatedRequest);
    }

    public void deleteRequest(Long id) {

        ServiceRequest entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Service request not found with id: " + id));

        repository.delete(entity);
    }

    private ServiceRequestResponse toResponse(ServiceRequest entity) {

        return new ServiceRequestResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPriority(),
                entity.getStatus()
        );
    }
}
