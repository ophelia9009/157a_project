# User Management API Documentation

This document describes the RESTful API endpoints for user management in the application.

## Base URL
All endpoints are relative to `/api/users`

## Endpoints

### Register New User
- **Method**: POST
- **Path**: `/register`
- **Request Body**:
  ```json
  {
    "username": "newuser",
    "password": "securePassword123",
    "email": "newuser@example.com"
  }
  ```
- **Success Response (201 Created)**:
  ```json
  {
    "userId": 101,
    "username": "newuser",
    "email": "newuser@example.com",
    "registerDate": "2025-04-28T18:48:00Z"
  }
  ```
- **Error Response (400 Bad Request)**:
  ```json
  {
    "error": "Validation failed",
    "details": ["Username must be at least 4 characters"]
  }
  ```

### User Login
- **Method**: POST  
- **Path**: `/login`
- **Request Body**:
  ```json
  {
    "username": "existinguser",
    "password": "userPassword123"
  }
  ```
- **Success Response (200 OK)**:
  ```json
  {
    "userId": 100,
    "username": "existinguser",
    "email": "user@example.com",
    "registerDate": "2025-04-27T10:30:00Z"
  }
  ```
- **Error Response (401 Unauthorized)**:
  ```json
  {
    "error": "Invalid credentials"
  }
  ```

### Get User Profile
- **Method**: GET
- **Path**: `/{userId}`
- **Success Response (200 OK)**:
  ```json
  {
    "userId": 100,
    "username": "existinguser",
    "email": "user@example.com",
    "registerDate": "2025-04-27T10:30:00Z"
  }
  ```
- **Error Response (404 Not Found)**:
  ```json
  {
    "error": "User not found"
  }
  ```

### Update User Profile
- **Method**: PUT
- **Path**: `/{userId}`
- **Request Body**:
  ```json
  {
    "username": "updateduser",
    "email": "newemail@example.com"
  }
  ```
- **Success Response (200 OK)**:
  ```json
  {
    "userId": 100,
    "username": "updateduser",
    "email": "newemail@example.com",
    "registerDate": "2025-04-27T10:30:00Z"
  }
  ```
- **Error Response (400 Bad Request)**:
  ```json
  {
    "error": "Email format invalid"
  }
  ```

### Delete User Account
- **Method**: DELETE
- **Path**: `/{userId}`
- **Success Response**: 204 No Content (empty body)
- **Error Response (404 Not Found)**:
  ```json
  {
    "error": "User not found"
  }
  ```

## Data Model
User information is stored with these fields:
- UserID (auto-generated)
- Username (unique)
- Password (hashed)
- Email
- RegisterDate (auto-generated)

## Example Implementation
See `UsersServlet.java` for reference implementation pattern.