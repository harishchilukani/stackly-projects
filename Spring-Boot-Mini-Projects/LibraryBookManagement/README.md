# Library Book Management System

## Description
A RESTful web application developed using Java and Spring Boot to manage library books. The application uses an in-memory ArrayList instead of a database.

## Features
- Add new books.
- View all books.
- Find books by ID.
- Update book details.
- Update book availability.
- Delete books.
- Search books by name and author.
- Filter available and unavailable books.
- Filter books by price.
- Sort books by name and price.
- Prevent duplicate book entries.
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
4. Run `LibraryBookManagementApplication.java`.
5. Test the REST APIs using Postman.
