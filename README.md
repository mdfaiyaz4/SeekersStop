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