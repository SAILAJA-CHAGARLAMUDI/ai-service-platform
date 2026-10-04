package com.sailaja.aiserviceplatform.service;

import com.sailaja.aiserviceplatform.entity.ServiceRequest;
import com.sailaja.aiserviceplatform.repository.ServiceRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository repository;

    public ServiceRequestService(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    public List<ServiceRequest> getAllRequests() {
        return repository.findAll();
    }

    public ServiceRequest createRequest(ServiceRequest request) {
        return repository.save(request);
    }
}
