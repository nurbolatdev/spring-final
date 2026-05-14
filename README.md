# Online Course Platform

A REST API backend for an online course platform built with Spring Boot.

## Tech Stack

- Java 21
- Spring Boot 4.0.6
- Spring Security 7 + JWT
- PostgreSQL
- Gradle
- Lombok + MapStruct
- SpringDoc OpenAPI (Swagger UI)

## Features

- JWT-based authentication (register / login)
- Role-based access control: ADMIN, TEACHER, STUDENT
- Course management with pagination, search, and category filter
- Lesson management per course
- Student enrollment and unenrollment
- Progress tracking per course
- File upload and download for course materials
- Async email notifications on register and enrollment
- Certificate generation (async)

## API Endpoints

| Method | Path | Description | Auth |
|--------|------|-------------|------|
| POST | /auth/register | Register new user | Public |
| POST | /auth/login | Login and get JWT | Public |
| GET | /courses | List courses (pageable) | Public |
| GET | /courses/{id} | Get course by id | Public |
| POST | /courses | Create course | TEACHER/ADMIN |
| PUT | /courses/{id} | Update course | TEACHER/ADMIN |
| DELETE | /courses/{id} | Delete course | TEACHER/ADMIN |
| GET | /categories | List categories | Public |
| POST | /categories | Create category | ADMIN |
| GET | /lessons/course/{id} | Get lessons for course | Public |
| POST | /lessons | Create lesson | TEACHER/ADMIN |
| POST | /enrollments/course/{id} | Enroll in course | Student |
| GET | /enrollments/my | My enrollments | Student |
| DELETE | /enrollments/course/{id} | Unenroll | Student |
| GET | /progress/course/{id} | Get progress | Authenticated |
| PUT | /progress/course/{id} | Update progress | Authenticated |
| POST | /files/upload/course/{id} | Upload file | TEACHER/ADMIN |
| GET | /files/download/{id} | Download file | Authenticated |

## Running Locally

**Requirements:** Java 21, PostgreSQL

1. Create database:
```sql
CREATE USER courseuser WITH PASSWORD 'coursepassword';
CREATE DATABASE coursedb OWNER courseuser;
```

2. Run:
```bash
./gradlew bootRun
```

3. Swagger UI: http://localhost:8080/swagger-ui.html

## Running with Docker

```bash
docker-compose up --build
```

## Default Admin

After startup, a default admin account is seeded:
- Email: `admin@course.com`
- Password: `admin123`
