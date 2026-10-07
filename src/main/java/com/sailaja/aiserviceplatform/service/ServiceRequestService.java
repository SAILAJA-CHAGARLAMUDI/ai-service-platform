package com.sailaja.aiserviceplatform.service;

import com.sailaja.aiserviceplatform.dto.ServiceRequestRequest;
import com.sailaja.aiserviceplatform.dto.ServiceRequestResponse;
import com.sailaja.aiserviceplatform.entity.ServiceRequest;
import com.sailaja.aiserviceplatform.repository.ServiceRequestRepository;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import com.sailaja.aiserviceplatform.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import com.sailaja.aiserviceplatform.kafka.ServiceRequestEvent;
import com.sailaja.aiserviceplatform.kafka.ServiceRequestEventProducer;

import java.time.LocalDateTime;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository repository;
    private final ServiceRequestEventProducer eventProducer;

    public ServiceRequestService(
            ServiceRequestRepository repository,
            ServiceRequestEventProducer eventProducer) {

        this.repository = repository;
        this.eventProducer = eventProducer;
    }
    @Cacheable(value = "serviceRequests", key = "'all'")
    public List<ServiceRequestResponse> getAllRequests() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Cacheable(value = "serviceRequests", key = "#id")
    public ServiceRequestResponse getRequestById(Long id) {
        ServiceRequest request = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Service request not found with id: " + id));

        return toResponse(request);
    }

    @CacheEvict(value = "serviceRequests", key = "'all'")
    public ServiceRequestResponse createRequest(ServiceRequestRequest request) {

        ServiceRequest entity = new ServiceRequest();

        entity.setTitle(request.title());
        entity.setDescription(request.description());
        entity.setPriority(request.priority());
        entity.setStatus(request.status());

        ServiceRequest savedRequest = repository.save(entity);

        ServiceRequestEvent event = new ServiceRequestEvent(
                "SERVICE_REQUEST_CREATED",
                savedRequest.getId(),
                LocalDateTime.now(),
                "service-request-service"
        );

        eventProducer.sendEvent(event);

        return toResponse(savedRequest);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "serviceRequests", key = "#id"),
                    @CacheEvict(value = "serviceRequests", key = "'all'")
            }
    )
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

    @Caching(
            evict = {
                    @CacheEvict(value = "serviceRequests", key = "#id"),
                    @CacheEvict(value = "serviceRequests", key = "'all'")
            }
    )
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
