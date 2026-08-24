# SeekersStop

A role-based job portal backend built with Java and Spring Boot. SeekersStop connects job seekers and recruiters through a secure REST API, providing functionality for user authentication, job management, recruiter and job seeker profiles, company management, and job applications.

## Overview

SeekersStop is designed around two primary user roles:

- **Job Seeker** — can create and manage a professional profile, browse jobs, apply for jobs, and track applications.
- **Recruiter** — can manage a recruiter profile and company, create and manage job postings, view applications, and update application statuses.

The application follows a layered architecture using Controllers, Services, Repositories, DTOs, and Entities.

## Features

### Authentication & Security

- User registration and login
- JWT-based authentication
- BCrypt password hashing
- Role-based authorization
- Separate permissions for `JOB_SEEKER` and `RECRUITER`
- JWT request filtering using `OncePerRequestFilter`
- Protected REST endpoints
- Centralized handling of authentication and authorization errors

### Job Seeker

Job seekers can:

- Create a job seeker profile
- View their own profile
- Update their profile
- Store skills, experience, CV, and contact information
- Browse available jobs
- View individual job details
- Apply for jobs
- View their applications
- View individual application details

### Recruiter

Recruiters can:

- Create a recruiter profile
- View their own profile
- Update their profile
- Manage their associated company
- Create job postings
- Update job postings
- Activate and deactivate job postings
- View applications for their jobs
- Update application statuses

### Company Management

Recruiters can manage the company associated with their account.

Company operations include:

- Create company
- View company information
- Update company information

Company access is tied to the authenticated recruiter, preventing arbitrary access to another company's information.

### Job Management

Recruiters can create and manage job postings.

Each job contains:

- Job title
- Description
- Required experience
- Qualification
- Salary
- Location
- Application deadline

Jobs can also be activated or deactivated without permanently deleting the job record.

### Application Management

The application system connects job seekers with recruiters.

Job seekers can submit applications for available jobs, while recruiters can review applications associated with their job postings and update their status.

Application status changes are handled through a dedicated API endpoint.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Backend framework |
| Spring MVC | REST API development |
| Spring Security | Authentication and authorization |
| JWT | Stateless authentication |
| Spring Data JPA | Data access layer |
| Hibernate | ORM |
| MySQL | Relational database |
| Jakarta Validation | Request validation |
| Lombok | Boilerplate reduction |
| Maven | Build and dependency management |
| Postman | API testing |
| Git & GitHub | Version control |

## Architecture

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

### Controller Layer

Responsible for:

- Receiving HTTP requests
- Mapping API endpoints
- Validating request DTOs
- Returning API responses

### Service Layer

Contains the application's business logic and coordinates operations between controllers and repositories.

### Repository Layer

Handles database persistence using Spring Data JPA.

### DTO Layer

DTOs are used to separate API request/response models from database entities.

### Security Layer

Spring Security and JWT handle authentication, authorization, and access control.

## API Endpoints

### Authentication

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/auth/register` | Public |
| `POST` | `/auth/login` | Public |

### Job Seeker

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/jobseeker/profile` | `JOB_SEEKER` |
| `GET` | `/jobseeker/profile` | `JOB_SEEKER` |
| `PUT` | `/jobseeker/profile` | `JOB_SEEKER` |

### Recruiter

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/recruiter/profile` | `RECRUITER` |
| `GET` | `/recruiter/profile` | `RECRUITER` |
| `PUT` | `/recruiter/profile` | `RECRUITER` |

### Company

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/company` | `RECRUITER` |
| `GET` | `/company` | `RECRUITER` |
| `PUT` | `/company` | `RECRUITER` |

### Jobs

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/jobs` | `RECRUITER` |
| `GET` | `/jobs` | Authenticated |
| `GET` | `/jobs/{id}` | Authenticated |
| `PUT` | `/jobs/{id}` | `RECRUITER` |
| `DELETE` | `/jobs/deactive/{id}` | `RECRUITER` |
| `PUT` | `/jobs/active/{id}` | `RECRUITER` |

### Applications

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/applications` | `JOB_SEEKER` |
| `GET` | `/applications/my` | `JOB_SEEKER` |
| `GET` | `/applications/{applicationId}` | `JOB_SEEKER` |
| `GET` | `/applications/recruiter` | `RECRUITER` |
| `PATCH` | `/applications/{applicationId}/status` | `RECRUITER` |

## Validation & Error Handling

The application uses **Jakarta Bean Validation** to validate incoming request data.

Common validation annotations include:

- `@NotBlank`
- `@NotNull`
- `@Positive`
- `@Pattern`
- `@Valid`

A centralized exception handling mechanism is used to provide consistent API error responses.

The application handles common HTTP responses such as:

| Status | Meaning |
|---|---|
| `200 OK` | Request completed successfully |
| `201 CREATED` | Resource successfully created |
| `204 NO CONTENT` | Operation successful with no response body |
| `400 BAD REQUEST` | Invalid request or validation failure |
| `401 UNAUTHORIZED` | Authentication is missing or invalid |
| `403 FORBIDDEN` | User is authenticated but lacks permission |
| `404 NOT FOUND` | Requested resource does not exist |
| `409 CONFLICT` | Resource conflicts or duplicate data |

## Authentication Flow

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

## Database

SeekersStop uses **MySQL** with **Spring Data JPA** and **Hibernate**.

The main database used during development is:

```text
job_portal
```

The application uses JPA entities to represent the main domain objects, including:

- `User`
- `JobSeeker`
- `Recruiter`
- `Company`
- `Job`
- `Application`

Hibernate manages the persistence and relationships between these entities.

## Environment Configuration

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

These values should be configured through the local environment or IDE run configuration.

> **Important:** Never commit database credentials, JWT secrets, API keys, or other sensitive information to GitHub.

The `.env` file is excluded through `.gitignore`.

## Getting Started

### Prerequisites

Make sure you have the following installed:

- Java 21
- MySQL
- Git

Maven is not required separately because the project includes the **Maven Wrapper**.

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/SeekersStop.git
cd SeekersStop
```

### 2. Create the Database

Create the MySQL database:

```sql
CREATE DATABASE job_portal;
```

### 3. Configure Environment Variables

Set the following variables in your IDE or local environment:

```text
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_secret_key
```

### 4. Run the Application

On Windows:

```bash
mvnw.cmd spring-boot:run
```

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

## Testing the API

The REST APIs can be tested using **Postman**.

A typical authentication flow is:

```text
Register
   ↓
Login
   ↓
Copy JWT
   ↓
Send JWT with protected requests
```

For protected endpoints, include the token in the request header:

```text
Authorization: Bearer <JWT_TOKEN>
```

Example:

```http
GET /applications/recruiter

Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## Development Progress

The project is being developed incrementally, with each feature being implemented and tested before being added to version control.

Current backend functionality includes:

- User authentication
- JWT security
- Role-based authorization
- Job seeker profiles
- Recruiter profiles
- Company management
- Job creation and management
- Job activation and deactivation
- Job applications
- Application status management
- Request validation
- Global exception handling
- MySQL persistence

## Future Improvements

Planned improvements include:

- Pagination
- Job search and filtering
- Sorting
- Resume file upload
- Email notifications
- Recruiter dashboard
- Job seeker dashboard
- Swagger / OpenAPI documentation
- Unit testing
- Integration testing
- Dockerization
- CI/CD pipeline
- Cloud deployment

## Author

**MD Faiyaz**

B.Tech — Computer Science & Technology

## License

This project is currently developed as a personal learning and portfolio project.