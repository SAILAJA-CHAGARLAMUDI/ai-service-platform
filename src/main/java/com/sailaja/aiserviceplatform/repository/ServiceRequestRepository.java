package com.sailaja.aiserviceplatform.repository;

import com.sailaja.aiserviceplatform.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
}
