# SeekersStop

A role-based job portal backend built with **Java 21 and Spring Boot**. SeekersStop connects job seekers and recruiters through a secure REST API, providing functionality for user authentication, job management, recruiter and job seeker profiles, company management, job applications, and CV management.

The backend is containerized using **Docker and Docker Compose**, allowing the Spring Boot application and MySQL database to run together in an isolated environment.

---

## Overview

SeekersStop is designed around two primary user roles:

* **Job Seeker** — can create and manage a professional profile, browse jobs, apply for jobs, and track applications.
* **Recruiter** — can manage a recruiter profile and company, create and manage job postings, view applications, and update application statuses.

The application follows a layered architecture using:

* Controllers
* Services
* Repositories
* DTOs
* Entities

---

## Features

### Authentication & Security

* User registration and login
* JWT-based authentication
* BCrypt password hashing
* Role-based authorization
* Separate permissions for `JOB_SEEKER` and `RECRUITER`
* JWT request filtering using `OncePerRequestFilter`
* Protected REST endpoints
* Centralized handling of authentication and authorization errors

### Job Seeker

Job seekers can:

* Create a job seeker profile
* View their own profile
* Update their profile
* Store skills, experience, CV, and contact information
* Browse available jobs
* View individual job details
* Apply for jobs
* View their applications
* View individual application details
* Upload and download CVs in PDF format

### Recruiter

Recruiters can:

* Create a recruiter profile
* View their own profile
* Update their profile
* Manage their associated company
* Create job postings
* Update job postings
* Activate and deactivate job postings
* View applications for their jobs
* Update application statuses

### Company Management

Recruiters can manage the company associated with their account.

Company operations include:

* Create company
* View company information
* Update company information

Company access is tied to the authenticated recruiter, preventing arbitrary access to another company's information.

### Job Management

Recruiters can create and manage job postings.

Job discovery supports:

* Pagination using Spring Data `Pageable`
* Dynamic sorting
* Filtering by location
* Keyword search by job title
* Filtering by experience
* Combining multiple filters in a single request
* Dynamic query construction using JPA Specifications

Each job contains:

* Job title
* Description
* Required experience
* Qualification
* Salary
* Location
* Application deadline

Jobs can also be activated or deactivated without permanently deleting the job record.

### Application Management

The application system connects job seekers with recruiters.

Job seekers can submit applications for available jobs, while recruiters can review applications associated with their job postings and update their status.

Application status changes are handled through a dedicated API endpoint.

### CV Management

Job seekers can upload their CV as a PDF file.

* PDF file type validation
* UUID-based file naming
* CV files stored outside the database
* CV reference stored in the database
* Authenticated CV download endpoint

---

# Technology Stack

| Technology             | Purpose                            |
| ---------------------- | ---------------------------------- |
| **Java 21**            | Programming language               |
| **Spring Boot**        | Backend framework                  |
| **Spring MVC**         | REST API development               |
| **Spring Security**    | Authentication and authorization   |
| **JWT**                | Stateless authentication           |
| **Spring Data JPA**    | Data access layer                  |
| **Hibernate**          | ORM                                |
| **MySQL 8**            | Relational database                |
| **Jakarta Validation** | Request validation                 |
| **Lombok**             | Boilerplate reduction              |
| **Maven**              | Build and dependency management    |
| **Swagger / OpenAPI**  | REST API documentation and testing |
| **Docker**             | Application containerization       |
| **Docker Compose**     | Multi-container orchestration      |
| **Postman**            | API testing                        |
| **Git & GitHub**       | Version control                    |

---

# Architecture

The project follows a layered architecture:

```text
Client
  |
  v
Controller Layer
  |
  v
Service Layer
  |
  v
Repository Layer
  |
  v
MySQL Database
```

### Docker Architecture

When running with Docker Compose:

```text
                 Docker Compose
                       |
          ┌────────────┴────────────┐
          |                         |
          v                         v
   Spring Boot App             MySQL 8
   seekersstop-app             seekersstop-mysql
          |                         |
          └────────────┬────────────┘
                       |
                  Docker Network
```

### Controller Layer

Responsible for:

* Receiving HTTP requests
* Mapping API endpoints
* Validating request DTOs
* Returning API responses

### Service Layer

Contains the application's business logic and coordinates operations between controllers and repositories.

### Repository Layer

Handles database persistence using Spring Data JPA.

The job repository uses `JpaSpecificationExecutor` for dynamic job filtering while retaining `JpaRepository` for standard CRUD operations.

### DTO Layer

DTOs are used to separate API request/response models from database entities.

### Security Layer

Spring Security and JWT handle authentication, authorization, and access control.

---

# API Documentation

SeekersStop uses **Swagger / OpenAPI** to provide interactive API documentation.

After starting the application, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger allows developers to:

* View available REST endpoints
* View request and response models
* Understand authentication requirements
* Test API endpoints directly from the browser

---

# API Endpoints

## Authentication

| Method | Endpoint         | Access |
| ------ | ---------------- | ------ |
| `POST` | `/auth/register` | Public |
| `POST` | `/auth/login`    | Public |

## Job Seeker

| Method | Endpoint             | Access       |
| ------ | -------------------- | ------------ |
| `POST` | `/jobseeker/profile` | `JOB_SEEKER` |
| `GET`  | `/jobseeker/profile` | `JOB_SEEKER` |
| `PUT`  | `/jobseeker/profile` | `JOB_SEEKER` |
| `GET`  | `/jobseeker/cv`      | `JOB_SEEKER` |

## Recruiter

| Method | Endpoint             | Access      |
| ------ | -------------------- | ----------- |
| `POST` | `/recruiter/profile` | `RECRUITER` |
| `GET`  | `/recruiter/profile` | `RECRUITER` |
| `PUT`  | `/recruiter/profile` | `RECRUITER` |

## Company

| Method | Endpoint   | Access      |
| ------ | ---------- | ----------- |
| `POST` | `/company` | `RECRUITER` |
| `GET`  | `/company` | `RECRUITER` |
| `PUT`  | `/company` | `RECRUITER` |

## Jobs

| Method   | Endpoint              | Access        |
| -------- | --------------------- | ------------- |
| `POST`   | `/jobs`               | `RECRUITER`   |
| `GET`    | `/jobs`               | Authenticated |
| `GET`    | `/jobs/{id}`          | Authenticated |
| `PUT`    | `/jobs/{id}`          | `RECRUITER`   |
| `DELETE` | `/jobs/deactive/{id}` | `RECRUITER`   |
| `PUT`    | `/jobs/active/{id}`   | `RECRUITER`   |

### Job Search, Filtering, Pagination & Sorting

The `GET /jobs` endpoint supports optional query parameters for job discovery.

Examples:

```text
GET /jobs
GET /jobs?location=Delhi
GET /jobs?title=Java
GET /jobs?experience=Fresher
GET /jobs?location=Delhi&title=Java
GET /jobs?location=Delhi&title=Java&experience=Fresher&page=0&size=5&sort=salary,desc
```

Supported parameters:

| Parameter | Purpose |
| --------- | ------- |
| `location` | Filters jobs by exact location |
| `title` | Searches job titles using partial matching |
| `experience` | Filters by exact experience value, such as `Fresher` |
| `page` | Page number, starting from `0` |
| `size` | Number of jobs returned per page |
| `sort` | Sorts results, for example `salary,desc` or `title,asc` |

If no filters are provided, only active jobs are returned.

Pagination and sorting are handled through Spring Data `Pageable`, while filtering is built dynamically using JPA Specifications.

## Applications

| Method  | Endpoint                               | Access       |
| ------- | -------------------------------------- | ------------ |
| `POST`  | `/applications`                        | `JOB_SEEKER` |
| `GET`   | `/applications/my`                     | `JOB_SEEKER` |
| `GET`   | `/applications/{applicationId}`        | `JOB_SEEKER` |
| `GET`   | `/applications/recruiter`              | `RECRUITER`  |
| `PATCH` | `/applications/{applicationId}/status` | `RECRUITER`  |

---

# Validation & Error Handling

The application uses **Jakarta Bean Validation** to validate incoming request data.

Common validation annotations include:

* `@NotBlank`
* `@NotNull`
* `@Positive`
* `@Pattern`
* `@Valid`

A centralized exception handling mechanism is used to provide consistent API error responses.

The application handles common HTTP responses such as:

| Status             | Meaning                                    |
| ------------------ | ------------------------------------------ |
| `200 OK`           | Request completed successfully             |
| `201 CREATED`      | Resource successfully created              |
| `204 NO CONTENT`   | Operation successful with no response body |
| `400 BAD REQUEST`  | Invalid request or validation failure      |
| `401 UNAUTHORIZED` | Authentication is missing or invalid       |
| `403 FORBIDDEN`    | User is authenticated but lacks permission |
| `404 NOT FOUND`    | Requested resource does not exist          |
| `409 CONFLICT`     | Resource conflicts or duplicate data       |

---

# Authentication Flow

The application uses **JWT for stateless authentication**.

```text
User
 |
 | Login
 v
AuthController
 |
 v
AuthService
 |
 | Validate credentials
 v
JwtService
 |
 | Generate JWT
 v
Client
 |
 | Authorization: Bearer <token>
 v
JwtFilter
 |
 | Validate token
 v
SecurityContext
 |
 v
Protected Controller
```

After successful authentication, the user's role is stored as a Spring Security authority:

```text
ROLE_JOB_SEEKER
```

or:

```text
ROLE_RECRUITER
```

Spring Security then uses these authorities to determine whether the user can access a particular endpoint.

---

# Database

SeekersStop uses **MySQL 8** with **Spring Data JPA** and **Hibernate**.

The main database is:

```text
job_portal
```

The application uses JPA entities to represent the main domain objects, including:

* `User`
* `JobSeeker`
* `Recruiter`
* `Company`
* `Job`
* `Application`

Hibernate manages the persistence and relationships between these entities.

When running through Docker Compose, MySQL data is stored in a Docker named volume:

```text
mysql-data
```

This allows database data to persist when the containers are stopped or recreated.

---

# Environment Configuration

Sensitive configuration values are not stored directly in the repository.

The application uses environment variables for:

```text
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

Example:

```text
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_secret_key
```

For Docker Compose, these values can be provided through a local `.env` file.

> **Important:** Never commit database credentials, JWT secrets, API keys, or other sensitive information to GitHub.

The `.env` file should remain local and be excluded through `.gitignore`.

A `.env.example` file can be provided in the repository to show the required variables:

```text
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_secret_key
```

---

# Running with Docker

Docker is the recommended way to run SeekersStop because it runs the Spring Boot application and MySQL database together.

## Prerequisites

Install:

* [Docker Desktop](https://www.docker.com/products/docker-desktop/)
* Git

## 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/SeekersStop.git
cd SeekersStop
```

## 2. Configure Environment Variables

Create a `.env` file in the same directory as `docker-compose.yml`:

```text
DB_USERNAME=seekersstop
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_secret_key
```

Do not commit this file to GitHub.

## 3. Start the Application

```bash
docker compose up --build
```

Or run it in the background:

```bash
docker compose up --build -d
```

Docker Compose will start:

```text
seekersstop-app
seekersstop-mysql
```

The Spring Boot application will be available at:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

## 4. Stop the Application

```bash
docker compose down
```

The MySQL data remains persisted in the Docker volume.

---

# Running Without Docker

The application can also be run directly using Java and MySQL.

## Prerequisites

* Java 21
* MySQL
* Git

Maven is not required separately because the project includes the **Maven Wrapper**.

## 1. Create the Database

Create the MySQL database:

```sql
CREATE DATABASE job_portal;
```

## 2. Configure Environment Variables

Set:

```text
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_secret_key
```

## 3. Run the Application

### Windows

```bash
mvnw.cmd spring-boot:run
```

### Linux/macOS

```bash
./mvnw spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Testing the API

The REST APIs can be tested using **Swagger UI** or **Postman**.

A typical authentication flow is:

```text
Register
   ↓
Login
   ↓
Copy JWT
   ↓
Authorize protected endpoints
   ↓
Send authenticated requests
```

For protected endpoints, include the token in the request header:

```text
Authorization: Bearer <JWT_TOKEN>
```

Swagger UI can also be used to interactively test protected endpoints after authentication.

---

# Development Progress

The project is being developed incrementally, with each feature being implemented and tested before being added to version control.

Current backend functionality includes:

* User authentication
* JWT security
* Role-based authorization
* Job seeker profiles
* Recruiter profiles
* Company management
* Job creation and management
* Job activation and deactivation
* Job applications
* Application status management
* Request validation
* Global exception handling
* MySQL persistence
* CV PDF upload and download
* Swagger / OpenAPI documentation
* Docker containerization
* Docker Compose setup
* Job pagination
* Dynamic job sorting
* Job search by title
* Job filtering by location
* Job filtering by experience
* Dynamic job filtering using JPA Specifications
* Combined job filters with pagination and sorting

---

# Future Improvements

Planned improvements include:

* Email notifications
* Recruiter dashboard
* Job seeker dashboard
* Unit testing
* Integration testing
* Redis caching
* Kafka-based event processing
* CI/CD pipeline
* Cloud deployment
* React frontend

---

# Author

**MD Faiyaz**

B.Tech — Computer Science & Technology

---

# License

This project is currently developed as a personal learning and portfolio project.
