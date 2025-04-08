🎓 EduSpark

A distributed microservices architecture for online course management, student enrollment, and exam processing — built with ❤️ using **Spring Boot**, **RabbitMQ**, **PostgreSQL**, **Feign**, **OpenFeign**, **Eureka**, **Spring Cloud Config**, and modern architecture patterns like **SAGA** & **Outbox**.

---

## 🧩 Services & Architecture

```
.
├── config-repo                  # Spring Cloud Config files (.yml)
├── services
│   ├── config-server            # Centralized configuration server
│   ├── discovery-server         # Eureka service discovery
│   ├── user-service             # Manages users (student / instructor)
│   ├── course-service           # Manages courses and enrollment
│   └── exam-service             # Handles exam submissions and grading
```

---

## ⚙️ Tech Stack

| Layer             | Tech Used                                   |
|------------------|---------------------------------------------|
| Language          | Java 17                                     |
| Framework         | Spring Boot, Spring Cloud, Spring Data JPA  |
| Communication     | RabbitMQ (Async Messaging via AMQP)         |
| API               | REST (with FeignClient between services)    |
| DB                | PostgreSQL , MongoDB                        |
| Saga Pattern      | Distributed transaction coordination        |
| Outbox Pattern    | Reliable async messaging via DB + Scheduler |
| Service Discovery | Eureka                                      |
| Config Management | Spring Cloud Config                         |
| Containerization  | Docker + Docker Compose                     |

---

## 🚀 Features Implemented

### ✅ Core Features:
- [x] Course creation and listing
- [x] Student enrollment into courses
- [x] Exam submission per course
- [x] Score recording per student

### 🧠 Distributed Saga Flow:
- [x] `EnrollStudentSagaStartedEvent` triggers async flow
- [x] Saga validates user role via UserService
- [x] Enrollment + rollback coordination
- [x] Saga failure scenarios handled via rollback events
- [x] Eventual consistency ensured

### 📦 Outbox Pattern:
- [x] `course_outbox_messages` table stores event payloads
- [x] Periodic scheduled dispatcher sends RabbitMQ messages
- [x] Failure-safe transactional messaging (bye bye inconsistency 👋)

### 🔁 Aimed Features:
- [ ] ✅ **Polling API** for frontend tracking
- [ ] 🔒 JWT-based authentication & authorization (via Gateway)
- [ ] 📚 API Gateway routing
- [ ] 🪄 Retry & Dead-letter queues for failed events
- [ ] 📈 Monitoring & Logging enhancements (Zipkin / Sleuth / ELK)
- [ ] 🧪 Integration tests for saga flows

---

## 📚 Saga Flow Diagram

```
[Student] --> [CourseService /api/enroll]
                      |
                      v
         +-------- Start Saga ---------+
         |                             |
         v                             |
 [Validate UserRole via UserService]   |
         |                             |
     [Update Course Entity]            |
         |                             |
  [Write to Outbox Table]              |
         |                             |
     [Scheduled Dispatcher] --(AMQP)--> [ExamService or RollbackQueue]
```

---

## 🐇 RabbitMQ Queues

| Exchange                  | Routing Key                                | Queue                             |
|--------------------------|---------------------------------------------|-----------------------------------|
| `course.internal.exchange` | `course.student.enrolled.routing-key`     | `course.student.enrolled.queue`  |
|                          | `course.exam.submitted.routing-key`        | `course.exam.submitted.queue`    |
|                          | `course.enroll.started.routing-key`        | `course.enroll.started.queue`    |
|                          | `course.student.rollback.routing-key`      | `course.student.rollback.queue`  |

---

## 🛠 How to Run Locally

1. ✅ Start **RabbitMQ** & **PostgreSQL** via Docker:
    ```bash
    docker-compose up -d
    ```

2. 🧠 Run config & discovery servers:
    ```
    ./mvnw spring-boot:run -f services/config-server
    ./mvnw spring-boot:run -f services/discovery-server
    ```

3. 💥 Run microservices:
    ```
    ./mvnw spring-boot:run -f services/user-service
    ./mvnw spring-boot:run -f services/course-service
    ./mvnw spring-boot:run -f services/exam-service
    ```

---

## 💬 Dev Notes

- **All events are handled using JSON-based messaging via RabbitMQ**
- **ObjectMapper** is shared for serialization/deserialization
- **Saga failures are logged to outbox as ENROLL_FAIL**
- **Processed messages are marked true after dispatch**
- Custom rollback logic can be expanded from listeners

---



## 🧠 Summary

This platform proves that even in complex distributed systems, **data consistency, reliability**, and **scalability** can be achieved with the right patterns:
- ✅ Saga Orchestration
- ✅ Outbox Pattern
- ✅ Async messaging with RabbitMQ 






