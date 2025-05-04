CREATE DATABASE IF NOT EXISTS sf_db;
USE sf_db;
DROP TABLE IF EXISTS `Subscriptions`;
DROP TABLE IF EXISTS `Comments`;
DROP TABLE IF EXISTS `Posts`;
DROP TABLE IF EXISTS `Subforums`;
DROP TABLE IF EXISTS `Users`;



CREATE TABLE `Users` (
  `UserID` INT NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(45) NOT NULL UNIQUE,
  `Password` VARCHAR(255) NOT NULL,
  `Email` VARCHAR(100) NOT NULL UNIQUE,
  `RegisterDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (`UserID`)
);

CREATE TABLE `Subforums` (
  `SubforumID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(100) NOT NULL UNIQUE,
  `Description` TEXT NOT NULL,
  `CreationDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `SubscriberCount` INT NOT NULL DEFAULT 0,
  `LastUpdated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `OwnerID` INT NOT NULL,
  PRIMARY KEY (`SubforumID`),
  CONSTRAINT `FK_User_SubforumID`
    FOREIGN KEY (`OwnerID`)
    REFERENCES `Users` (`UserID`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE
  );

CREATE TABLE `Posts` (
  `PostID` INT NOT NULL AUTO_INCREMENT,
  `Title` VARCHAR(255) NOT NULL,
  `BodyText` TEXT NOT NULL,
  `CreationDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Rating` INT NOT NULL DEFAULT 0,
  `UserID` INT NOT NULL, -- FK to Users table
  `SubforumID` INT NOT NULL, -- FK to Subforums table
  `LastUpdated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`PostID`),
  CONSTRAINT `FK_Posts_UserID`
    FOREIGN KEY (`UserID`)
    REFERENCES `Users` (`UserID`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `FK_Posts_SubforumID`
    FOREIGN KEY (`SubforumID`)
    REFERENCES `Subforums` (`SubforumID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );

CREATE TABLE `Comments` (
  `CommentID` INT NOT NULL AUTO_INCREMENT,
  `CommentText` TEXT NOT NULL,
  `CreationDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Rating` INT NOT NULL DEFAULT 0,
  `UserID` INT NOT NULL, -- FK to Users table
  `PostID` INT NOT NULL, -- FK to Posts table
  `ParentID` INT NOT NULL, -- FK to Comments table (self-reference for replies)
  `LastUpdated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`CommentID`),
  CONSTRAINT `FK_Comments_UserID`
    FOREIGN KEY (`UserID`)
    REFERENCES `Users` (`UserID`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `FK_Comments_PostID`
    FOREIGN KEY (`PostID`)
    REFERENCES `Posts` (`PostID`)
    ON DELETE CASCADE -- If post deleted, delete its comments
    ON UPDATE CASCADE
  );

ALTER TABLE `Comments` 
ADD CONSTRAINT `FK_Comments_ParentID`
  FOREIGN KEY (`ParentID`)
  REFERENCES `Comments` (`CommentID`)
  ON DELETE CASCADE -- If parent comment deleted, delete replies
  ON UPDATE CASCADE;

CREATE TABLE `Subscriptions` (
  `UserID` INT NOT NULL, -- FK to Users table
  `SubforumID` INT NOT NULL, -- FK to Subforums table
  `SubscriptionDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`UserID`, `SubforumID`),
  UNIQUE KEY (`UserID`, `SubforumID`), -- Prevent duplicate subscriptions
  CONSTRAINT `FK_Subscriptions_UserID`
    FOREIGN KEY (`UserID`)
    REFERENCES `Users` (`UserID`)
    ON DELETE CASCADE -- If user deleted, remove their subscriptions
    ON UPDATE CASCADE,
  CONSTRAINT `FK_Subscriptions_SubforumID`
    FOREIGN KEY (`SubforumID`)
    REFERENCES `Subforums` (`SubforumID`)
    ON DELETE CASCADE -- If subforum deleted, remove subscriptions
    ON UPDATE CASCADE
    );



CREATE USER 'appuser'@'%' IDENTIFIED BY 'Password!1';
GRANT ALL ON sf_db.* TO 'appuser'@'%';
