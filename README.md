# TrackThree

**TrackThree** is a lightweight fitness tracking application designed to log daily macros and hydration. Built with Spring Boot and PostgreSQL, this backend service provides a simple API for storing and retrieving calorie, protein, and water intake entries.

---

## Features

- RESTful API built with Spring Boot
- Logs daily fitness data including calories, protein, and water
- Persists data to PostgreSQL using JPA
- Follows standard controller repository service pattern
- Easily expandable for future features like authentication and analytics

---

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- HikariCP

### Development Tools

- Maven Wrapper
- DevTools for hot reloading
- H2 Console (enabled for debugging)
- Postman or any API client for testing

---

## Endpoints

- `POST /api/tracker/log`  
  Submits a new entry with date, calories, protein, and water

- `GET /api/tracker/history`  
  Retrieves all tracker entries from the database

---
