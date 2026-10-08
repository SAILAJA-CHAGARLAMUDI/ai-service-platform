# AI Service Platform

A production-oriented **Java 21 / Spring Boot service platform** demonstrating RESTful API development, PostgreSQL persistence, Redis caching, Kafka-based event-driven architecture, validation, centralized exception handling, and unit testing.

> **Current scope:** The project currently focuses on the backend foundation and event-driven architecture. AI/LLM capabilities are planned for a future milestone and are intentionally not included in the current implementation.

---

## 📌 Project Overview

The **AI Service Platform** is a backend service-management platform designed around a `Service Request` domain.

A service request represents an issue or task submitted to a service platform, including:

* Title
* Description
* Priority
* Status

The application exposes REST APIs for creating, retrieving, updating, and deleting service requests.

The platform also demonstrates:

* Relational data persistence using PostgreSQL
* DTO-based API design
* Request validation
* Centralized REST exception handling
* Redis-based caching
* Cache invalidation after data changes
* Kafka-based event-driven processing
* JSON event serialization/deserialization
* Unit testing with JUnit 5 and Mockito

---

# 🏗️ High-Level Architecture

```text
                         ┌─────────────────────────┐
                         │       REST Client        │
                         │ PowerShell / Postman     │
                         └────────────┬────────────┘
                                      │
                                      │ HTTP/JSON
                                      ▼
                         ┌─────────────────────────┐
                         │     Spring Boot API     │
                         │                         │
                         │  ServiceRequestController│
                         └────────────┬────────────┘
                                      │
                                      ▼
                         ┌─────────────────────────┐
                         │ ServiceRequestService   │
                         │                         │
                         │ Business Logic          │
                         │ Validation Flow         │
                         │ Cache Management        │
                         │ Event Publishing        │
                         └───────┬─────────┬───────┘
                                 │         │
                    ┌────────────┘         └──────────────┐
                    │                                     │
                    ▼                                     ▼
          ┌──────────────────┐                  ┌──────────────────┐
          │   PostgreSQL     │                  │      Redis       │
          │                  │                  │                  │
          │ Service Requests │                  │ Application      │
          │                  │                  │ Cache            │
          └──────────────────┘                  └──────────────────┘

                                 │
                                 │ ServiceRequestEvent
                                 ▼
                       ┌─────────────────────────┐
                       │         Kafka           │
                       │                         │
                       │ service-request-events  │
                       └────────────┬────────────┘
                                    │
                                    │ JSON Event
                                    ▼
                       ┌─────────────────────────┐
                       │ Kafka Event Consumer    │
                       │                         │
                       │ JSON → Java Event       │
                       └─────────────────────────┘
```

---

# 🔄 Event-Driven Flow

The application publishes Kafka events for service request CRUD operations.

### Create

```text
POST /api/service-requests
        │
        ▼
ServiceRequestService
        │
        ▼
PostgreSQL
        │
        │ Generated ID
        ▼
ServiceRequestEvent
        │
        │ SERVICE_REQUEST_CREATED
        ▼
Kafka
        │
        ▼
Kafka Consumer
```

### Update

```text
PUT /api/service-requests/{id}
        │
        ▼
PostgreSQL
        │
        ▼
ServiceRequestEvent
        │
        │ SERVICE_REQUEST_UPDATED
        ▼
Kafka
        │
        ▼
Kafka Consumer
```

### Delete

```text
DELETE /api/service-requests/{id}
        │
        ▼
PostgreSQL
        │
        ▼
ServiceRequestEvent
        │
        │ SERVICE_REQUEST_DELETED
        ▼
Kafka
        │
        ▼
Kafka Consumer
```

---

# 🛠️ Technology Stack

## Backend

| Technology         | Purpose                            |
| ------------------ | ---------------------------------- |
| Java 21            | Application development            |
| Spring Boot 4.1.1  | Application framework              |
| Spring Web         | REST APIs                          |
| Spring Data JPA    | Database access                    |
| Hibernate/JPA      | ORM                                |
| Jakarta Validation | Request validation                 |
| Jackson 3          | JSON serialization/deserialization |

## Database

| Technology | Purpose                     |
| ---------- | --------------------------- |
| PostgreSQL | Primary relational database |

## Caching

| Technology   | Purpose                       |
| ------------ | ----------------------------- |
| Redis 7      | Distributed application cache |
| Spring Cache | Cache abstraction             |

## Messaging

| Technology         | Purpose                |
| ------------------ | ---------------------- |
| Apache Kafka 4.1.0 | Event-driven messaging |
| Spring Kafka       | Kafka integration      |

## Testing

| Technology | Purpose                         |
| ---------- | ------------------------------- |
| JUnit 5    | Unit testing                    |
| Mockito    | Mocking and interaction testing |
| Maven      | Build and dependency management |

## Development Tools

* IntelliJ IDEA
* Git
* GitHub
* Docker
* Docker Desktop
* WSL2

---

# 📂 Project Structure

```text
ai-service-platform
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── sailaja
│   │   │           └── aiserviceplatform
│   │   │
│   │   │               ├── config
│   │   │               │   ├── KafkaProducerConfig.java
│   │   │               │   └── RedisConfig.java
│   │   │               │
│   │   │               ├── controller
│   │   │               │   ├── HelloController.java
│   │   │               │   └── ServiceRequestController.java
│   │   │               │
│   │   │               ├── dto
│   │   │               │   ├── ServiceRequestRequest.java
│   │   │               │   └── ServiceRequestResponse.java
│   │   │               │
│   │   │               ├── entity
│   │   │               │   └── ServiceRequest.java
│   │   │               │
│   │   │               ├── enums
│   │   │               │   ├── Priority.java
│   │   │               │   └── ServiceRequestStatus.java
│   │   │               │
│   │   │               ├── exception
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   └── ResourceNotFoundException.java
│   │   │               │
│   │   │               ├── kafka
│   │   │               │   ├── ServiceRequestEvent.java
│   │   │               │   ├── ServiceRequestEventConsumer.java
│   │   │               │   └── ServiceRequestEventProducer.java
│   │   │               │
│   │   │               ├── repository
│   │   │               │   └── ServiceRequestRepository.java
│   │   │               │
│   │   │               ├── service
│   │   │               │   └── ServiceRequestService.java
│   │   │               │
│   │   │               └── AiServicePlatformApplication.java
│   │   │
│   │   └── resources
│   │       └── application.properties
│   │
│   └── test
│       └── java
│           └── com
│               └── sailaja
│                   └── aiserviceplatform
│                       ├── AiServicePlatformApplicationTests.java
│                       └── service
│                           └── ServiceRequestServiceTest.java
│
├── pom.xml
└── README.md
```

---

# 📦 Core Domain Model

## ServiceRequest

The primary domain entity contains:

```text
ServiceRequest
├── id
├── title
├── description
├── priority
└── status
```

### Priority

```text
LOW
MEDIUM
HIGH
CRITICAL
```

### Status

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
```

---

# 🌐 REST API

Base URL:

```text
http://localhost:8080
```

## Create Service Request

```http
POST /api/service-requests
```

Example request:

```json
{
  "title": "Database connection issue",
  "description": "Unable to connect to production database",
  "priority": "HIGH",
  "status": "OPEN"
}
```

Example response:

```json
{
  "id": 1,
  "title": "Database connection issue",
  "description": "Unable to connect to production database",
  "priority": "HIGH",
  "status": "OPEN"
}
```

---

## Get All Service Requests

```http
GET /api/service-requests
```

---

## Get Service Request by ID

```http
GET /api/service-requests/{id}
```

Example:

```text
GET /api/service-requests/1
```

---

## Update Service Request

```http
PUT /api/service-requests/{id}
```

Example:

```json
{
  "title": "Updated database issue",
  "description": "Database connection timeout continues",
  "priority": "CRITICAL",
  "status": "IN_PROGRESS"
}
```

---

## Delete Service Request

```http
DELETE /api/service-requests/{id}
```

Successful deletion returns:

```text
HTTP 204 No Content
```

---

# ✅ Validation

The API validates incoming requests using Jakarta Bean Validation.

For example:

```java
@NotBlank
String title
```

and:

```java
@NotNull
Priority priority
```

Invalid requests return a structured `400 Bad Request` response.

Example:

```json
{
  "timestamp": "2026-10-07T18:00:00",
  "status": 400,
  "error": "Validation Failed",
  "messages": {
    "title": "Title is required",
    "priority": "Priority is required"
  }
}
```

Invalid enum values are also handled centrally.

For example:

```json
{
  "priority": "URGENT"
}
```

returns an error explaining the allowed values.

---

# 🚨 Exception Handling

The application uses a centralized:

```text
@RestControllerAdvice
```

through:

```text
GlobalExceptionHandler
```

Currently handled scenarios include:

* Resource not found
* Bean validation failures
* Invalid request body
* Invalid enum values

This keeps controller classes focused on HTTP/API concerns while providing consistent error responses.

---

# ⚡ Redis Caching

Redis is used to reduce repeated database reads.

The following service operations are cached:

```text
GET /api/service-requests
GET /api/service-requests/{id}
```

Cache entries use a 10-minute TTL.

### Cache Keys

All requests:

```text
serviceRequests::all
```

Individual request:

```text
serviceRequests::<id>
```

Example:

```text
serviceRequests::7
```

### Cache Invalidation

The application invalidates relevant cache entries when data changes.

Create:

```text
Create request
     ↓
Save PostgreSQL
     ↓
Evict "all" cache
```

Update:

```text
Update request
     ↓
Save PostgreSQL
     ↓
Evict individual cache
     ↓
Evict "all" cache
```

Delete:

```text
Delete request
     ↓
Delete PostgreSQL
     ↓
Evict individual cache
     ↓
Evict "all" cache
```

This prevents stale service-request data from remaining in Redis.

---

# 📨 Kafka Event Processing

Kafka topic:

```text
service-request-events
```

The application uses a typed event model:

```java
public record ServiceRequestEvent(
        String eventType,
        Long requestId,
        LocalDateTime timestamp,
        String source
) {
}
```

## Event Types

```text
SERVICE_REQUEST_CREATED
SERVICE_REQUEST_UPDATED
SERVICE_REQUEST_DELETED
```

Example event:

```json
{
  "eventType": "SERVICE_REQUEST_CREATED",
  "requestId": 7,
  "timestamp": "2026-10-07T18:03:13.162142600",
  "source": "service-request-service"
}
```

### Serialization

The producer uses Jackson to convert:

```text
ServiceRequestEvent
        ↓
JSON
```

The JSON is sent to Kafka as a string message.

### Deserialization

The consumer receives:

```text
JSON
```

and converts it back into:

```text
ServiceRequestEvent
```

using Jackson.

---

# 🔄 Kafka CRUD Event Example

For a request with ID `8`:

### CREATE

```text
SERVICE_REQUEST_CREATED
requestId = 8
```

### UPDATE

```text
SERVICE_REQUEST_UPDATED
requestId = 8
```

### DELETE

```text
SERVICE_REQUEST_DELETED
requestId = 8
```

The actual application flow has been tested through the REST API and Kafka consumer.

---

# 🧪 Testing

The project currently uses:

* JUnit 5
* Mockito
* Spring Boot test support

The service layer has unit tests covering:

### Create

* DTO to entity mapping
* Repository save
* Response mapping
* Kafka event publishing
* Kafka event contents

### Read

* Retrieve by ID
* Resource not found
* Retrieve all requests

### Update

* Existing request lookup
* Field updates
* Repository save
* Kafka event publishing
* Kafka event contents
* Resource not found

### Delete

* Existing request lookup
* Repository deletion
* Kafka event publishing
* Kafka event contents
* Resource not found

Run all tests:

```powershell
mvn clean test
```

Expected result:

```text
Tests run: 9
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

> Integration testing with real PostgreSQL/Redis/Kafka containers is intentionally deferred to a later milestone.

---

# 🐳 Local Infrastructure

The current development environment uses Docker for infrastructure services.

## PostgreSQL

```text
Host: localhost
Port: 5432
Database: ai_service_platform
```

## Redis

```text
Container: ai-service-redis
Image: redis:7
Port: 6379
```

## Kafka

```text
Container: ai-service-kafka
Image: apache/kafka:4.1.0
Port: 9092
```

Kafka topic:

```text
service-request-events
```

---

# ⚙️ Configuration

Database credentials are intentionally **not stored in the repository**.

The application uses environment variables:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ai_service_platform
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Redis:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

Kafka:

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=ai-service-platform
spring.kafka.consumer.auto-offset-reset=earliest
```

### Environment Variables

Windows PowerShell:

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="<your-password>"
```

Do **not** commit passwords or other secrets to GitHub.

---

# 🚀 How to Run Locally

## Prerequisites

Install:

* Java 21
* Maven
* PostgreSQL
* Docker Desktop
* Git

Verify Java:

```powershell
java -version
```

Verify Maven:

```powershell
mvn -version
```

Verify Docker:

```powershell
docker --version
```

---

## 1. Clone the Repository

```powershell
git clone https://github.com/SAILAJA-CHAGARLAMUDI/ai-service-platform
```

Navigate into the project:

```powershell
cd ai-service-platform
```

---

## 2. Start PostgreSQL

Create the database:

```text
ai_service_platform
```

Make sure PostgreSQL is running on:

```text
localhost:5432
```

---

## 3. Start Redis

Example:

```powershell
docker start ai-service-redis
```

Verify:

```powershell
docker exec -it ai-service-redis redis-cli ping
```

Expected:

```text
PONG
```

---

## 4. Start Kafka

Example:

```powershell
docker start ai-service-kafka
```

Make sure Kafka is available on:

```text
localhost:9092
```

The application expects the topic:

```text
service-request-events
```

---

## 5. Configure Database Credentials

Set:

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="<your-password>"
```

---

## 6. Run the Application

```powershell
mvn spring-boot:run
```

The application starts on:

```text
http://localhost:8080
```

---

# 🧪 Test the Application

## Create

```powershell
$body = '{"title":"Database issue","description":"Unable to connect to database","priority":"HIGH","status":"OPEN"}'; Invoke-RestMethod -Method POST -Uri "http://localhost:8080/api/service-requests" -ContentType "application/json" -Body $body
```

## Get All

```powershell
Invoke-RestMethod -Method GET -Uri "http://localhost:8080/api/service-requests"
```

## Get by ID

```powershell
Invoke-RestMethod -Method GET -Uri "http://localhost:8080/api/service-requests/1"
```

## Update

```powershell
$body = '{"title":"Updated database issue","description":"Connection timeout continues","priority":"CRITICAL","status":"IN_PROGRESS"}'; Invoke-RestMethod -Method PUT -Uri "http://localhost:8080/api/service-requests/1" -ContentType "application/json" -Body $body
```

## Delete

```powershell
Invoke-RestMethod -Method DELETE -Uri "http://localhost:8080/api/service-requests/1"
```

---

# 🔍 Verifying Kafka Events

When a service request is created, updated, or deleted, the Spring Boot console should display the corresponding event.

Example:

```text
Received event: ServiceRequestEvent[
    eventType=SERVICE_REQUEST_CREATED,
    requestId=7,
    timestamp=2026-10-07T18:03:13.162142600,
    source=service-request-service
]
```

This confirms the event was:

```text
Business Operation
       ↓
Kafka Producer
       ↓
Kafka Topic
       ↓
Kafka Consumer
       ↓
Java ServiceRequestEvent
```

---

# 🔐 Security Considerations

The current version intentionally does not include authentication or authorization.

The project does, however, follow some basic security practices:

* Database credentials are supplied through environment variables
* Secrets are not stored in source code
* Sensitive credentials were removed from Git history
* API input validation is enabled
* Invalid requests are handled centrally

Future versions will introduce:

* Spring Security
* JWT authentication
* Role-based access control
* API authorization

---

# 📊 Current Implementation Status

| Capability                | Status      |
| ------------------------- | ----------- |
| Java 21                   | ✅ Complete  |
| Spring Boot               | ✅ Complete  |
| REST APIs                 | ✅ Complete  |
| PostgreSQL persistence    | ✅ Complete  |
| JPA/Hibernate             | ✅ Complete  |
| DTO layer                 | ✅ Complete  |
| Request validation        | ✅ Complete  |
| Global exception handling | ✅ Complete  |
| JUnit 5                   | ✅ Complete  |
| Mockito                   | ✅ Complete  |
| Redis caching             | ✅ Complete  |
| Cache invalidation        | ✅ Complete  |
| Kafka producer            | ✅ Complete  |
| Kafka consumer            | ✅ Complete  |
| Typed Kafka events        | ✅ Complete  |
| JSON serialization        | ✅ Complete  |
| JSON deserialization      | ✅ Complete  |
| CREATE Kafka event        | ✅ Complete  |
| UPDATE Kafka event        | ✅ Complete  |
| DELETE Kafka event        | ✅ Complete  |
| Integration testing       | ⏸️ Deferred |
| Kafka retry/DLT           | 🔜 Planned  |
| Spring AI / LLM           | 🔜 Planned  |
| RAG / Vector Search       | 🔜 Planned  |
| React / TypeScript        | 🔜 Planned  |
| Redux                     | 🔜 Planned  |
| Spring Security / JWT     | 🔜 Planned  |
| Dockerized application    | 🔜 Planned  |
| CI/CD                     | 🔜 Planned  |
| Kubernetes                | 🔜 Planned  |

---

# 🗺️ Future Roadmap

The project will evolve incrementally toward an AI-enabled service platform.

### Phase 1 — Backend Foundation

* REST APIs
* PostgreSQL
* Validation
* Exception handling
* Unit testing

**Status: Complete**

### Phase 2 — Performance

* Redis caching
* Cache invalidation

**Status: Complete**

### Phase 3 — Event-Driven Architecture

* Kafka producer
* Kafka consumer
* Typed events
* CRUD events
* Retry handling
* Dead Letter Topics

**Current phase**

### Phase 4 — AI Integration

Planned capabilities:

* Spring AI
* LLM integration
* Service-request summarization
* Automatic priority/category suggestions
* AI-generated troubleshooting recommendations
* Natural-language service-request analysis

### Phase 5 — RAG

Planned:

* Embeddings
* Vector database
* Knowledge-base ingestion
* Retrieval-Augmented Generation
* Context-aware troubleshooting

### Phase 6 — Frontend

Planned:

* React
* TypeScript
* Redux
* Service-request dashboard
* AI-assisted request analysis

### Phase 7 — Security

Planned:

* Spring Security
* JWT
* RBAC
* Secured REST APIs

### Phase 8 — Cloud & DevOps

Planned:

* Docker
* CI/CD
* Kubernetes
* Cloud deployment
* Observability

---

# 🎯 Learning Objectives

This project is also designed as a hands-on learning project for modern Java backend and AI engineering.

Key concepts demonstrated so far:

* Java 21 development
* Spring Boot application architecture
* REST API design
* Layered architecture
* DTO pattern
* JPA/Hibernate
* PostgreSQL
* Bean Validation
* Global exception handling
* Unit testing
* Mockito interaction testing
* Redis caching
* Cache invalidation
* Kafka
* Event-driven architecture
* JSON serialization/deserialization
* Domain event modeling

Future milestones will add AI/LLM and cloud-native capabilities.

---

# 👩‍💻 Author

**Sailaja Chagarlamudi**

Full Stack Java Developer

Areas of focus:

* Java
* Spring Boot
* Microservices
* REST APIs
* Event-driven architecture
* Kafka
* Redis
* PostgreSQL
* React / TypeScript
* AI / LLM integration
* Cloud-native application development
