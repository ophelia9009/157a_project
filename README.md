# This is a project for course CS157a.

To run the frontend: 
```
$ cd frontend
$ npm run dev
```

To run the backend: 
```
$ cd backend
$ 
```
**Requirements:**
Functional Requirements:

- User Management:
    - [x] Allow new users to register for an account.
    - [x] Allow registered users to log in. 
    - [x] Allow registered users to view their own profile information.
    - [ ] Allow registered users to update their own profile information.
    - [x] Allow registered users to delete their account.
    - [x] Store user information (UserID, Username, Password, Email, RegisterDate) in a database.
    - [x] whatever it is , it will use "select all from users" for example, user list page
- Subforum Management:
    - [x] Allow registered users to create new subforums.
    - [x] Allow registered users to edit the subforums they created. (Note: Proposal states "edit and delete own sub-forums" under Functional Req, but schema doesn't explicitly list an owner UserID for Subforums - this might need clarification in the project).
    - [x] Allow registered users to subscribe subforums.
    - [ ] Allow registered users to search subforums based on specific conditions. @Vaishu
    - [x] Store subforum information (SubforumID, Name, CreationDate, Description, SubscriberCount, lastUpdated) in a database.
- Post Management:
    - [x] Allow registered users to create new posts within a specific subforum. 
    - [x] Allow registered users to edit the posts they created.                 @Johnathan
    - [x] Allow registered users to delete the posts they created.               @Johnathan
    - [x] Allow registered users to view posts within a subforum.  
    - [x] Store post information (PostID, UserID, Title, BodyText, CreationDate, SubforumID, Rating) in a database.
- Comment Management:
    - [x] Allow registered users to create comments (reply) on posts.
    - [x] Allow registered users to edit the comments they created.
    - [x] Allow registered users to delete the comments they created.            
    - [x] Allow registered users to view comments on posts.
    - [x] Store comment information (CommentID, CommentText, UserID, CreationDate, Rating, PostID, ParentID, lastUpdated) in a database.

**TODO:**

- Data Layer: Database Design
    - [ ] ER Diagram
    - [ ] Schema Definition
    - [ ] Normalization
    - [ ] Set up MySQL database with structured schema, foreign keys, and non-null constraints
    - [ ] Create the database tables (at least 4 tables) 
    - [ ] Initialize the different tables (at least 15 entries per table) appropriately: No fields cannot be null.
    - [ ] Issue the following SQL statements:
        - [ ] Select all to display existing information
        - [ ] Add new
        - [ ] Edit/Update the existing information
        - [ ] Delete existing information

- Application Layer: JDBC-powered DAO classes
    * Set Up Java Project: Use build tool Maven, add the MySQL Connector/J library as a dependency.
    * Implement Database Connection Utility
    * Create Model Class and Data Access Object
    * Test and confirm the connection to database and retrieval of data from the database

- Presentation Layer: for user interactions
