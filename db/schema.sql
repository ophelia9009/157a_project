CREATE sf_db;
USE sf_db;
CREATE TABLE `Users` (
  `UserID` INT NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(45) NOT NULL,
  `Password` VARCHAR(255) NOT NULL,
  `Email` VARCHAR(100) NOT NULL,
  `RegisterDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `Email_UNIQUE` (`Email`)
);

CREATE TABLE Subforums (
    SubforumID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL UNIQUE,
    Description TEXT,
    CreationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    SubscriberCount INT DEFAULT 0,
    UserID INT NOT NULL, -- Added: To know who created the subforum
    lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) -- Link to the creator
);

CREATE TABLE Posts (
    PostID INT AUTO_INCREMENT PRIMARY KEY,
    Title VARCHAR(255) NOT NULL,
    BodyText TEXT,
    CreationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Rating INT DEFAULT 0,
    UserID INT NOT NULL, -- FK to Users table
    SubforumID INT NOT NULL, -- FK to Subforums table
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (SubforumID) REFERENCES Subforums(SubforumID) ON DELETE CASCADE -- If subforum deleted, delete its posts
);

CREATE TABLE Comments (
    CommentID INT AUTO_INCREMENT PRIMARY KEY,
    CommentText TEXT NOT NULL,
    CreationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Rating INT DEFAULT 0,
    UserID INT NOT NULL, -- FK to Users table
    PostID INT NOT NULL, -- FK to Posts table
    ParentID INT NULL, -- FK to Comments table (self-reference for replies)
    lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (PostID) REFERENCES Posts(PostID) ON DELETE CASCADE, -- If post deleted, delete its comments
    FOREIGN KEY (ParentID) REFERENCES Comments(CommentID) ON DELETE CASCADE -- If parent comment deleted, delete replies
);

CREATE TABLE Subscriptions (
    SubscriptionID INT AUTO_INCREMENT PRIMARY KEY, -- Surrogate key
    UserID INT NOT NULL, -- FK to Users table
    SubforumID INT NOT NULL, -- FK to Subforums table
    SubscriptionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE, -- If user deleted, remove their subscriptions
    FOREIGN KEY (SubforumID) REFERENCES Subforums(SubforumID) ON DELETE CASCADE, -- If subforum deleted, remove subscriptions
    UNIQUE KEY unique_subscription (UserID, SubforumID) -- Prevent duplicate subscriptions
);