package com.sailaja.aiserviceplatform.entity;

import jakarta.persistence.*;
import com.sailaja.aiserviceplatform.enums.Priority;
import com.sailaja.aiserviceplatform.enums.ServiceRequestStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "service_requests")
public class ServiceRequest {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

        private String description;

        @Enumerated(EnumType.STRING)
        private Priority priority;

        @Enumerated(EnumType.STRING)
        private ServiceRequestStatus status;

        public ServiceRequest() {
        }

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Priority  getPriority() {
            return priority;
        }

        public void setPriority(Priority  priority) {
            this.priority = priority;
        }

        public ServiceRequestStatus  getStatus() {
            return status;
        }

        public void setStatus(ServiceRequestStatus  status) {
            this.status = status;
        }
}
