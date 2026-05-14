# Course Booking API

> REST API backend for managing course registrations — users can browse, book, and cancel spots in scheduled courses.

---

## Features

- User management with role-based access control (USER / ADMIN)
- Course management with configurable participant limits and date ranges
- Booking system with BOOK and CANCEL statuses
- Automatic participant count tracking via booking relationships
- Query courses by current date or booking status

## Tech Stack

**Backend:** Java 17, Spring Boot 2.7.5  
**Database:** Apache Derby (embedded), H2 (tests)  
**ORM:** Spring Data JPA / Hibernate  
**Build:** Maven  
**Other:** Spring Security, Apache Commons Lang 3

## Frontend

A Vue.js frontend is planned as a separate repository to complete the full-stack picture. This repo focuses on the backend API and is fully testable via Postman, curl, or any HTTP client.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | /api/user | Register a new user (public) |
| GET | /api/user | Get all users (USER / ADMIN) |
| GET | /api/user/{id} | Get user by ID |
| GET | /api/user/byUserName/{userName} | Get user by username |
| PUT | /api/user | Update user (USER / ADMIN) |
| DELETE | /api/user/{id} | Delete user by ID (ADMIN) |
| DELETE | /api/user/byUserName/{userName} | Delete user by username (ADMIN) |
| POST | /api/course | Create a new course (USER / ADMIN) |
| GET | /api/course | Get all courses (USER / ADMIN) |
| GET | /api/course/{id} | Get course by ID |
| GET | /api/course/byTitle/{title} | Get course by title |
| GET | /api/course/booked | Get courses that have at least one booking |
| GET | /api/course/currentdate | Get courses running on today's date |
| PUT | /api/course | Update course (USER / ADMIN) |
| DELETE | /api/course/{id} | Delete course by ID (ADMIN) |
| PATCH | /api/course/booking | Create or cancel a booking (USER / ADMIN) |

### Testing the API

All endpoints can be tested via Postman, Insomnia, or curl. Example:

```bash
# Register a new user
curl -X POST http://localhost:8081/api/user \
  -H "Content-Type: application/json" \
  -d '{"userName":"philipp","password":"secret123","email":"philipp@example.com"}'

# List courses (with Basic Auth)
curl -u philipp:secret123 http://localhost:8081/api/course
```

## Data Model

A `User` can have many `Booking` records; a `Course` can also have many `Booking` records. `Booking` uses a composite primary key (`BookingId`) made up of `courseId` and `userId`, enforcing that a user can only hold one booking per course. Each booking carries a `status` (BOOK or CANCEL) and the date it was created. Deleting a `Course` cascades to all its bookings via orphan removal.

## Architecture Highlights

Each CRUD operation is split into its own Controller and Service class (e.g., `CreateCourseController` + `CreateCourseService`), keeping handlers small and single-purpose. Request and response shapes are decoupled from entities through dedicated DTOs — passwords are never included in response bodies, and `CourseResponseBody` computes `currentParticipants` dynamically. A `GlobalExceptionHandler` (@ControllerAdvice) maps every custom exception (`CourseFullException`, `UserNotFoundException`, etc.) to the appropriate HTTP status code and a consistent `ResponseBody` envelope with `message` and `errorMessage` lists. Security is stateless HTTP Basic Auth backed by BCrypt password hashing; `CourseUserDetailsService` loads users from the database and wires roles directly into Spring Security's `UserDetails`.

## Local Setup

### Prerequisites
- Java 17+
- Maven 3+
- No external database required — Apache Derby runs embedded

### Configuration
Update `codersbay-courseApp/src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:derby:local;create=true
spring.datasource.username=derbyuser
spring.datasource.password=password
server.port=8081
```

### Run
```bash
git clone https://github.com/philippreischer/courseapp-backend.git
cd courseapp-backend
mvn spring-boot:run -pl codersbay-courseApp
```

API runs on `http://localhost:8081`

## Background

Built as my backend graduation project at CodersBay Vienna. The goal was to design and implement a complete REST API from scratch — covering data modeling with JPA relationships, a clean layered architecture, custom exception handling, and stateless authentication with Spring Security. A Vue.js frontend is planned to complement the backend.

## Status

Core CRUD and booking logic complete, including role-based security and global exception handling. Vue.js frontend integration planned as next step.

---

**Author:** Philipp Reischer · [Portfolio](https://philippreischer.github.io)