package com.sailaja.aiserviceplatform.service;

import com.sailaja.aiserviceplatform.kafka.ServiceRequestEvent;
import com.sailaja.aiserviceplatform.repository.ServiceRequestRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import com.sailaja.aiserviceplatform.dto.ServiceRequestRequest;
import com.sailaja.aiserviceplatform.dto.ServiceRequestResponse;
import com.sailaja.aiserviceplatform.entity.ServiceRequest;
import com.sailaja.aiserviceplatform.enums.Priority;
import com.sailaja.aiserviceplatform.enums.ServiceRequestStatus;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import org.mockito.ArgumentCaptor;
import com.sailaja.aiserviceplatform.exception.ResourceNotFoundException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.sailaja.aiserviceplatform.kafka.ServiceRequestEventProducer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ServiceRequestServiceTest {

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private ServiceRequestEventProducer eventProducer;

    private ServiceRequestService service;

    @Test
    void testCreateRequest() {

        service = new ServiceRequestService(repository, eventProducer);
        ServiceRequestRequest request = new ServiceRequestRequest(
                "Database connection issue",
                "Unable to connect to production database",
                Priority.HIGH,
                ServiceRequestStatus.OPEN
        );

        ServiceRequest savedEntity = mock(ServiceRequest.class);

        when(savedEntity.getId()).thenReturn(1L);
        when(savedEntity.getTitle()).thenReturn("Database connection issue");
        when(savedEntity.getDescription())
                .thenReturn("Unable to connect to production database");
        when(savedEntity.getPriority()).thenReturn(Priority.HIGH);
        when(savedEntity.getStatus()).thenReturn(ServiceRequestStatus.OPEN);

        when(repository.save(any(ServiceRequest.class)))
                .thenReturn(savedEntity);

        ServiceRequestResponse response = service.createRequest(request);

        ArgumentCaptor<ServiceRequestEvent> eventCaptor =
                ArgumentCaptor.forClass(ServiceRequestEvent.class);

        verify(eventProducer).sendEvent(eventCaptor.capture());

        ServiceRequestEvent event = eventCaptor.getValue();

        assertEquals("SERVICE_REQUEST_CREATED", event.eventType());
        assertEquals(1L, event.requestId());
        assertNotNull(event.timestamp());
        assertEquals("service-request-service", event.source());

        assertEquals("Database connection issue", response.title());
        assertEquals(Priority.HIGH, response.priority());
        assertEquals(ServiceRequestStatus.OPEN, response.status());

        ArgumentCaptor<ServiceRequest> serviceRequestCaptor =
                ArgumentCaptor.forClass(ServiceRequest.class);

        verify(repository).save(serviceRequestCaptor.capture());

        ServiceRequest capturedEntity = serviceRequestCaptor.getValue();

        assertEquals("Database connection issue", capturedEntity.getTitle());
        assertEquals(
                "Unable to connect to production database",
                capturedEntity.getDescription()
        );
        assertEquals(Priority.HIGH, capturedEntity.getPriority());
        assertEquals(ServiceRequestStatus.OPEN, capturedEntity.getStatus());

    }

    @Test
    void testGetRequestById() {

        service = new ServiceRequestService(repository, eventProducer);

        ServiceRequest entity = new ServiceRequest();
        entity.setTitle("Database connection issue");
        entity.setDescription("Unable to connect to production database");
        entity.setPriority(Priority.HIGH);
        entity.setStatus(ServiceRequestStatus.OPEN);

        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        ServiceRequestResponse response = service.getRequestById(1L);

        assertEquals("Database connection issue", response.title());
        assertEquals(
                "Unable to connect to production database",
                response.description()
        );
        assertEquals(Priority.HIGH, response.priority());
        assertEquals(ServiceRequestStatus.OPEN, response.status());

        verify(repository).findById(1L);
    }

    @Test
    void testGetRequestById_notFound() {

        service = new ServiceRequestService(repository, eventProducer);

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getRequestById(999L)
        );

        assertEquals(
                "Service request not found with id: 999",
                exception.getMessage()
        );

        verify(repository).findById(999L);
    }

    @Test
    void testGetAllRequests() {

        service = new ServiceRequestService(repository, eventProducer);

        ServiceRequest firstRequest = new ServiceRequest();
        firstRequest.setTitle("Database connection issue");
        firstRequest.setDescription("Unable to connect to production database");
        firstRequest.setPriority(Priority.HIGH);
        firstRequest.setStatus(ServiceRequestStatus.OPEN);

        ServiceRequest secondRequest = new ServiceRequest();
        secondRequest.setTitle("API timeout issue");
        secondRequest.setDescription("External API is timing out");
        secondRequest.setPriority(Priority.CRITICAL);
        secondRequest.setStatus(ServiceRequestStatus.IN_PROGRESS);

        when(repository.findAll())
                .thenReturn(List.of(firstRequest, secondRequest));

        List<ServiceRequestResponse> responses =
                service.getAllRequests();

        assertEquals(2, responses.size());

        assertEquals("Database connection issue", responses.get(0).title());
        assertEquals(Priority.HIGH, responses.get(0).priority());
        assertEquals(ServiceRequestStatus.OPEN, responses.get(0).status());

        assertEquals("API timeout issue", responses.get(1).title());
        assertEquals(Priority.CRITICAL, responses.get(1).priority());
        assertEquals(ServiceRequestStatus.IN_PROGRESS, responses.get(1).status());

        verify(repository).findAll();
    }

    @Test
    void testUpdateRequest() {

        service = new ServiceRequestService(repository, eventProducer);

        ServiceRequest existingRequest = spy(new ServiceRequest());

        doReturn(1L).when(existingRequest).getId();

        existingRequest.setTitle("Old title");
        existingRequest.setDescription("Old description");
        existingRequest.setPriority(Priority.LOW);
        existingRequest.setStatus(ServiceRequestStatus.OPEN);

        ServiceRequestRequest updateRequest = new ServiceRequestRequest(
                "Updated title",
                "Updated description",
                Priority.CRITICAL,
                ServiceRequestStatus.IN_PROGRESS
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(existingRequest));

        when(repository.save(any(ServiceRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ServiceRequestResponse response =
                service.updateRequest(1L, updateRequest);

        assertEquals("Updated title", response.title());
        assertEquals("Updated description", response.description());
        assertEquals(Priority.CRITICAL, response.priority());
        assertEquals(ServiceRequestStatus.IN_PROGRESS, response.status());

        verify(repository).findById(1L);
        verify(repository).save(existingRequest);

        ArgumentCaptor<ServiceRequestEvent> eventCaptor =
                ArgumentCaptor.forClass(ServiceRequestEvent.class);

        verify(eventProducer).sendEvent(eventCaptor.capture());

        ServiceRequestEvent event = eventCaptor.getValue();

        assertEquals("SERVICE_REQUEST_UPDATED", event.eventType());
        assertEquals(1L, event.requestId());
        assertNotNull(event.timestamp());
        assertEquals("service-request-service", event.source());
    }

    @Test
    void testUpdateRequest_notFound() {

        service = new ServiceRequestService(repository, eventProducer);

        ServiceRequestRequest updateRequest = new ServiceRequestRequest(
                "Updated title",
                "Updated description",
                Priority.HIGH,
                ServiceRequestStatus.IN_PROGRESS
        );

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateRequest(999L, updateRequest)
        );

        assertEquals(
                "Service request not found with id: 999",
                exception.getMessage()
        );

        verify(repository).findById(999L);
    }

    @Test
    void testDeleteRequest() {

        service = new ServiceRequestService(repository, eventProducer);

        ServiceRequest existingRequest = new ServiceRequest();
        existingRequest.setTitle("Database connection issue");
        existingRequest.setDescription("Unable to connect to production database");
        existingRequest.setPriority(Priority.HIGH);
        existingRequest.setStatus(ServiceRequestStatus.OPEN);

        when(repository.findById(1L))
                .thenReturn(Optional.of(existingRequest));

        service.deleteRequest(1L);

        verify(repository).findById(1L);
        verify(repository).delete(existingRequest);

        ArgumentCaptor<ServiceRequestEvent> eventCaptor =
                ArgumentCaptor.forClass(ServiceRequestEvent.class);

        verify(eventProducer).sendEvent(eventCaptor.capture());

        ServiceRequestEvent event = eventCaptor.getValue();

        assertEquals("SERVICE_REQUEST_DELETED", event.eventType());
        assertEquals(1L, event.requestId());
        assertNotNull(event.timestamp());
        assertEquals("service-request-service", event.source());
    }

    @Test
    void testDeleteRequest_notFound() {

        service = new ServiceRequestService(repository, eventProducer);

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteRequest(999L)
        );

        assertEquals(
                "Service request not found with id: 999",
                exception.getMessage()
        );

        verify(repository).findById(999L);
    }
}