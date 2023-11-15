# Doubt Resolution System

## Overview

The Doubt Resolution System is a platform that facilitates communication between students and tutors to address academic doubts effectively.


## Table of Contents

 [Doubt Resolution System](#doubt-resolution-system)
  - [Overview](#overview)
  - [Table of Contents](#table-of-contents)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
  - [Usage](#usage)
  - [Project Structure](#project-structure)
  - [Technologies Used](#technologies-used)

## Getting Started

### Prerequisites

Ensure you have the following prerequisites installed:

- Java 8 or higher
- Spring Boot
- MySQL Database

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Amanastel/Revly_assignment

### Netlify Link(Frontend):-
```bash
https://guileless-platypus-4d94fd.netlify.app/
```



## Usage
- Visit the API documentation at `http://localhost:8888/swagger-ui/` to explore available endpoints.
To use the Doubt Resolution System:

1. Create an account as a student or tutor.
2. Log in to the system.
3. Students can submit doubts, and tutors can respond to them.
4. Track the status of doubts, mark them as resolved, or request clarification.


## Project Structure

The project is organized into the following key directories:

- `src/main/java/com/revly/`: Contains the main Java source code.
- `src/main/resources/`: Includes configuration files and static resources.
- `src/test/`: Houses test classes.


## Technologies Used

- Spring Boot
- Java Persistence API (JPA)
- MySQL Database
- Spring Security
- HTML CSS Javascript


## Important Endpoints

### 1. Submit Doubt Request

**Endpoint:**

- **Method:** POST
- **URL:** `/users/doubtRequest`
- **Description:** Allows students to submit a doubt request.
- **Request Body:**
  ```json
  {
    "doubtSubject": "MATHS",
    "doubtDescription": "I need help with algebraic equations."
  }

**Response:**

- **Status:** 200 OK

- **Body:**

```json

{
  "id": 1,
  "student": {
    "id": 123,
    "name": "Student Name",
    "email": "student@example.com",
    "userType": "STUDENT"
  },
  "doubtSubject": "MATHS",
  "timestamp": "2023-11-10T12:30:45",
  "doubtDescription": "I need help with algebraic equations.",
  "doubtResolved": "UNRESOLVED"
}
```


### 2. Get All Resolved Doubt Requests for Tutor

**Endpoint:**

- **Method:** GET
- **URL:** `/users/doubtRequest/tutor/resolved`
- **Description:** Retrieve all resolved doubt requests for a tutor.

**Response:**

- **Status:** 200 OK

- **Body:**

```json
[
  {
    "id": 2,
    "student": {
      "id": 456,
      "name": "Another Student",
      "email": "another.student@example.com",
      "userType": "STUDENT"
    },
    "doubtSubject": "PHYSICS",
    "timestamp": "2023-11-10T14:45:30",
    "doubtDescription": "I had a question about Newton's laws.",
    "doubtResolved": "RESOLVED",
    "tutor": {
      "id": 789,
      "name": "Tutor Name",
      "email": "tutor@example.com",
      "userType": "TUTOR",
      "subjectExpertise": "PHYSICS"
    }
  },
  // Additional resolved doubt requests...
]

```
## Contributing

We welcome contributions! If you want to contribute to the project, please follow these guidelines:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and commit them with descriptive messages.
4. Push your changes to your fork.
5. Submit a pull request.
