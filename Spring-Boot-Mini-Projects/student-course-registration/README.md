# Student Course Registration System

## Description
A RESTful web application developed using Java and Spring Boot to manage student course registrations. It uses an in-memory ArrayList to store student details.

## Features
- Register new students.
- View all registered students.
- Find students by ID.
- Update student details.
- Delete student registrations.
- Search students by name.
- Search students by course.
- Search students by email.
- Filter students by age range.
- Sort students by name and age.
- Prevent duplicate email registrations.
- Display course-wise student count.
- Input validation and global exception handling.
- AOP logging for service methods.

## Technologies Used
- Java 17
- Spring Boot 3.5.7
- Spring Web
- Spring Validation
- Spring AOP
- Lombok
- Maven
- Postman

## Storage
In-memory ArrayList. No database required.

## How to Run
1. Clone or download the repository.
2. Open the project in IntelliJ IDEA.
3. Reload Maven dependencies.
4. Run `StudentCourseRegistrationApplication.java`.
5. Test the REST APIs using Postman.
