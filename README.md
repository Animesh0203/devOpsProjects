# Student API Project

> A RESTful API to manage a student database, providing CRUD (Create, Read, Update, Delete) operations on student records. 

---

## Table of Contents

- [Project Overview](#project-overview)
- [Technologies Used](#technologies-used)
- [API Endpoints](#api-endpoints)
- [Technical Architecture](#technical-architecture)
- [Prerequisites](#prerequisites)


---

## Project Overview

This project is a RESTful API developed to perform CRUD operations on a list of students. Each student record contains the following attributes:

- *ID* (integer): Unique identifier for each student
- *Name* (string): Full name of the student
- *Age* (integer): Age of the student
- *Email* (string): Contact email of the student

The API is designed to support concurrent access with in-memory data storage for fast access, utilizing Java's ConcurrentHashMap for thread safety. Additionally, the API integrates with the *Ollama API* to generate AI-based summaries for each student's profile.

> *Note:* It’s important to note that Ollama depends on your system’s specifications. For optimal performance, Ollama requires a high-core system to handle the AI model processing. While I may not be able to fully demonstrate the summary output on this machine, rest assured that it works on systems with sufficient resources and is configured to provide a summary whenever a student's details are requested.

---

## Technologies Used

- *Java Spring Boot*: Framework for building and running the REST API
- *Ollama API*: For AI-based profile summaries
- *Maven*: Dependency and project management

### Technical Details

- *Thread Safety*: Leveraging ConcurrentHashMap and AtomicInteger ensures thread safety, allowing multiple users to perform operations concurrently without race conditions.
- *Asynchronous API Calls*: Used to handle Ollama API requests asynchronously, preventing delays in CRUD operations.
- *Exception Handling*: Custom error handling for user-friendly API responses and better debugging.
- *Validation*: Input validation on the student's name, age, and email fields to ensure data consistency.

---

## API Endpoints

1. *Create Student* - POST /students  
   Create a new student record.

2. *Get Student by ID* - GET /students/{id}  
   Retrieve details of a specific student.

3. *Update Student* - PUT /students/{id}  
   Update an existing student record.

4. *Delete Student* - DELETE /students/{id}  
   Remove a student record.


---

## Technical Architecture

The project follows a *layered architecture* to ensure separation of concerns:

- *Controller Layer*: Defines RESTful endpoints and manages incoming requests.
- *Service Layer*: Contains business logic for CRUD operations and interacts with the Ollama API.
- *Repository Layer*: In-memory storage using ConcurrentHashMap.
- *Exception Handling*: Custom error responses for better API usability.

The project also leverages *Java's ExecutorService* for managing asynchronous calls to the Ollama API, ensuring high performance without blocking CRUD operations.

---

## Prerequisites

- *Java*: Version 11 or higher
- *Maven*: For building and running the project

---

