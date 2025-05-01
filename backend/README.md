# Run Backend

1. Install Tomcat using [Homebrew](https://formulae.brew.sh/formula/tomcat) or downloading [tomcat zip](https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.104/bin/apache-tomcat-9.0.104.zip) directly
2. Get tomcat installation path, (for example if installed using Homebrew, path looks like this `/opt/homebrew/Cellar/tomcat/11.0.6/libexec/`)
3. Install "Smart Tomcat" IntelliJ IDEA plugin from marketplace ("Settings..." -> "Plugins" -> Search "Smart Tomcat" in marketplace)
4. Configure "Smart Tomcat" IntelliJ IDEA plugin
5. Click Green Arrow Icon to Run



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
- **Method**: PATCH
- **Path**: `/{userId}`
- **Request Body** (partial updates allowed):
  ```json
  {
    "password": "newpassword123",
    "email": "newemail@example.com"
  }
  ```
- **Behavior**:
  - Only password and email fields are updated
  - Username and registration date remain unchanged
  - Missing fields preserve their current values
- **Success Response (200 OK)**:
  ```json
  {
    "userId": 100,
    "username": "originaluser",  // Unchanged
    "email": "newemail@example.com",  // Updated
    "registerDate": "2025-04-27T10:30:00Z"  // Unchanged
  }
  ```
- **Error Responses**:
  - 400 Bad Request (invalid email format):
  ```json
  {
    "error": "Email format invalid"
  }
  ```
  - 404 Not Found (user doesn't exist):
  ```json
  {
    "error": "User not found"
  }
  ```

### Delete User Account
- **Method**: DELETE
- **Path**: `/{userId}`
- **Behavior**:
  - Permanently deletes the user account with the specified ID
  - Returns empty response on success
  - Returns error if user doesn't exist
- **Success Response**:
  - Status: 204 No Content
  - Body: Empty
- **Error Responses**:
  - 400 Bad Request (invalid user ID format):
  ```json
  {
    "error": "Delete failed",
    "details": "Invalid user ID"
  }
  ```
  - 404 Not Found (user doesn't exist):
  ```json
  {
    "error": "User not found"
  }
  ```
- **Example Request**:
  ```http
  DELETE /api/users/123 HTTP/1.1
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


# Subforum Management API Documentation

This document describes the RESTful API endpoints for subforum management in the application.

## Base URL
All endpoints are relative to `/api/subforums`

## Endpoints

### Register New Subforum
- **Method**: POST
- **Path**: `/`
- **Request Body**:
  ```json
  {
    "Name": "newsubforum",
    "Description": "newdescription"
  }
  ```
- **Success Response (201 Created)**:
  ```json
  {
    "SubforumID": 101,
    "Name": "newsubforum",
    "Description": "newdescription",
    "CreationDate": "2025-04-29T18:48:00Z"
  }
  ```
- **Error Response (400 Bad Request)**:
  ```json
  {
    "error": "Validation failed",
    "details": ["Name invalid (must be unique)"]
  }

### Get Subforum View
- **Method**: GET
- **Path**: `/{SubforumID}`
- **Success Response (200 OK)**:
  ```json
  {
    "SubforumID": 101,
    "Name": "existingsubforum",
    "Description": "description",
    "CreationDate": "2025-04-29T18:48:00Z"
  }
  ```
- **Error Response (404 Not Found)**:
  ```json
  {
    "error": "Subforum {SubforumID} not found"
  }
  ```

### Update Subforum
- **Method**: PATCH
- **Path**: `/{SubforumID}`
- **Request Body**:
  ```json
  {
    "Name": "updatedsubforum",
    "Description": "updateddescription"
  }
  ```
- **Success Response (200 OK)**:
  ```json
  {
    "SubforumID": 101,
    "Name": "updatedsubforum",
    "Description": "updateddescription",
    "LastUpdated": "2025-04-30T18:48:00Z"
  }
  ```
- **Error Response (400 Bad Request)**:
  ```json
  {
    "error": "Name invalid (must be unique)"
  }
  ```

### Delete Subforum 
- **Method**: DELETE
- **Path**: `/{SubforumID}`
- **Success Response**: 204 No Content (empty body)
- **Error Response (404 Not Found)**:
  ```json
  {
    "error": "Subforum {SubforumID} not found"
  }

## Data Model
Subforum information is stored with these fields:
- SubforumID (auto-generated)
- Name (unique)
- Description
- CreationDate (auto-generated)
- SubscriberCount (count of Subscriptions with this SubforumID)
- LastUpdated (auto-generated)

## Example Implementation
See `SubforumsServlet.java` for reference implementation pattern.