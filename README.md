\# SeekersStop



A role-based job portal backend built with \*\*Java, Spring Boot, Spring Security, JWT, Spring Data JPA, Hibernate, and MySQL\*\*.



SeekersStop provides separate functionality for \*\*Job Seekers\*\* and \*\*Recruiters\*\*, allowing job seekers to manage their profiles and applications while recruiters can manage companies, create and manage job postings, and process applications.



\---



\## 🚀 Features



\### 🔐 Authentication \& Authorization



\- User registration

\- User login

\- JWT-based authentication

\- Secure password hashing using BCrypt

\- Role-based authorization

\- Separate access control for:

&#x20; - `JOB\_SEEKER`

&#x20; - `RECRUITER`

\- JWT authentication filter using `OncePerRequestFilter`

\- Protected REST APIs

\- Unauthorized and forbidden request handling



\---



\### 👤 Job Seeker



Job seekers can:



\- Create their job seeker profile

\- View their own profile

\- Update their profile

\- Store:

&#x20; - Name

&#x20; - Skills

&#x20; - Experience

&#x20; - CV

&#x20; - Contact information

\- Browse available jobs

\- View individual job details

\- Apply for jobs

\- View their applications

\- View individual application details



\---



\### 🏢 Recruiter



Recruiters can:



\- Create their recruiter profile

\- View their own recruiter profile

\- Update their recruiter profile

\- Manage their associated company

\- Create job postings

\- Update job postings

\- View job postings

\- View individual job details

\- Deactivate job postings

\- Reactivate job postings

\- View applications received for their jobs

\- Change application status



\---



\### 🏭 Company Management



Recruiters can manage their associated company profile.



Company information includes:



\- Company name

\- Description

\- Location

\- Website

\- Contact information



Available operations:



\- Get company profile

\- Update company profile



The company is accessed through the authenticated recruiter's account rather than allowing users to arbitrarily access another company's information.



\---



\### 💼 Job Management



Recruiters can create and manage job postings.



A job contains:



\- Job title

\- Description

\- Required experience

\- Qualification

\- Salary

\- Location

\- Application deadline



Job lifecycle:



```text

ACTIVE

&#x20;  │

&#x20;  │ Deactivate

&#x20;  ▼

INACTIVE

&#x20;  │

&#x20;  │ Activate

&#x20;  ▼

ACTIVE



Recruiters can deactivate a job when it should no longer be available and reactivate it when required.



📄 Job Applications



Job seekers can apply for jobs.



Recruiters can:



View applications associated with their jobs

View individual applications

Update application status



Application status management is handled through a dedicated endpoint.



🛡️ Validation



The project uses Jakarta Bean Validation to validate incoming request DTOs.



Examples include:



@NotBlank

@NotNull

@Positive

@Pattern



Example validation:



@NotBlank(message = "Job title is required")

private String title;



@NotNull(message = "Job salary is required")

@Positive(message = "Salary cannot be less than 0")

private Double salary;



Validation errors are handled centrally instead of writing the same error-handling logic inside every controller.

