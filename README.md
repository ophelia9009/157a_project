# This is a project for course CS157a.
Project Overview: 
We created a 3 tier architecture social media forum based web application that took inspiration from the widely popular “front page of the internet” aka Reddit. We used a tech stack of Java and its JDBC API to connect to MySQL database and displayed in Jakarta Server Pages (JSP) with additional functionality in JavaScript. Additional logic is also encapsulated in servlets and the project is deployed on Apache Tomcat. The project’s purpose is to emphasize communities and topics within subforums that are populated by users, posts, and comments. Joining engaging discussions, keeping up to date with the latest news, and Q&A are envisioned relevant usages for individual end-users of the application. Further developments of fondness and a sense of belonging to the people and subforums who engage with each other can foster communities. With time, such interconnectedness can have overall positive benefits for the members. For example, if one wanted to start a project for more programming experience and to put on their resume, they could start by reaching out to subforums that have people with related interests. If they wanted to create an application like this, reaching out to the MySQL, Java, JavaScript, etc. subforums and recruiting teammates and mentors to develop with and learn from should be feasible. This is only one of the many benefits that this project could bring when thriving active communities develop with time.

Set up the MySQL database with the files in /db/. 

To run the backend: 
```
$ cd backend
$ mvn jetty:run
```
```
OR
Click run button on Intellij IDEA
```
![image](https://github.com/user-attachments/assets/3a2f300d-f3d0-4c33-909b-93929dc70e02)
```
 with this configuration setup
```
![image](https://github.com/user-attachments/assets/85fa5e97-4a42-4a03-b8d7-2f52b13b96a8)

**Requirements:**
Functional Requirements:

- User Management:
    - [x] Allow new users to register for an account.
    - [x] Allow registered users to log in. 
    - [x] Allow registered users to view their own profile information.
    - [x] Allow registered users to update their own profile information.
    - [x] Allow registered users to delete their account.
    - [x] Store user information (UserID, Username, Password, Email, RegisterDate) in a database.
    - [x] whatever it is , it will use "select all from users" for example, user list page
- Subforum Management:
    - [x] Allow registered users to create new subforums.
    - [x] Allow registered users to edit the subforums they created. (Note: Proposal states "edit and delete own sub-forums" under Functional Req, but schema     
          doesn't explicitly list an owner UserID for Subforums - this might need clarification in the project).
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


- Data Layer: Database Design
    - [x] ER Diagram
    - [x] Schema Definition
    - [x] Normalization
    - [x] Set up MySQL database with structured schema, foreign keys, and non-null constraints
    - [x] Create the database tables (at least 4 tables) 
    - [x] Initialize the different tables (at least 15 entries per table) appropriately: No fields cannot be null.
    - [x] Issue the following SQL statements:
        - [x] Select all to display existing information
        - [x] Add new
        - [x] Edit/Update the existing information
        - [x] Delete existing information

- Application Layer: JDBC-powered DAO classes
    - [x] Set Up Java Project: Use build tool Maven, add the MySQL Connector/J library as a dependency.
    - [x] Implement Database Connection Utility (JDBC)
    - [x] Create Model Class and Data Access Object
    - [x] Test and confirm the connection to database and retrieval of data from the database

- Presentation Layer: for user interactions
    - [x] Implement Jakarta Server Pages (JSP)
    - [x] Create Servlet Classes
    - [x] Test and confirm the operations and interactions from the end user side.
