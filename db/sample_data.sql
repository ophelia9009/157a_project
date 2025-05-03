INSERT INTO `sf_db`.`users`
(`Username`, `Password`, `Email`, `RegisterDate`)
VALUES
('flower_girl', 'flowery_pin', 'floweringNow123@gmail.com', '2017-01-01 00:00:00'),
('flow_boy', 'flow_in_my_hair', 'flowNow123@gmail.com', '2017-01-01 00:00:00'),
('DriedSquid', 'InkAwayUrWelcome', 'WutRUthINKING@gmail.com', '2017-01-02 00:00:00'),
('YayPokemon', 'PikachuICU', 'blastingoffagain@gmail.com', '2017-02-01 00:00:00'),
('DBenthusiast', 'NONOSQL!!', 'relationalOrNOTHING@yahoo.com', '2016-01-01 00:00:00'),
('whatAREDBsss', 'wellTHEY', 'storedata@gmail.com', '2015-02-01 00:00:00'),
('spaceCowboy', 'snakeInMyCode!', 'howdyPartian?@gmail.com', '2017-01-01 00:00:00'),
('JavaFTW', 'SE8orBUST.', 'exceptionHandlingRN@yahoo.com', '2006-01-01 00:00:00'),
('AsFarAsTheIcanC', '++InTheChat', 'AmIRiteFellas@gmail.com', '2011-01-01 00:00:00'),
('CharmanderI', 'choose?', 'charmeleon!!!wut@gmail.com', '2017-01-01 00:00:00'),
('MewThree', 'Let''s_SHOWDOWN!', 'betterthanArceus@gmail.com', '2015-01-01 00:00:00'),
('TromboneC#', 'YesToNoteTaking', 'YouLikeJazz?@gmail.com', '2010-01-07 00:00:00'),
('Flute_King', 'clarinetMitBbetter', 'ILikeFruit@gmail.com', '2014-08-01 00:00:00'),
('Oboe_Queen', 'flowery_pin', 'ILikeTurtles@gmail.com', '2016-03-02 00:00:00'),
('Viol_Koff', 'in_ragigus_ing', 'pokemonMusic!@gmail.com', '2016-01-01 00:00:00'),
('GenericUser1', 'GenericPassword1', 'generic1@gmail.com', '2016-01-01 00:00:00'),
#The subsequent generic users are AI-generated. Using the prompts on ChatGPT 4/25/25
#"generate generic users 2-15 in this format ('GenericUser1', 'GenericPassword1', 'generic1@gmail.com', '2024-01-01 00:00:00')"
#"change up the timestamps"
('GenericUser2', 'GenericPassword2', 'generic2@gmail.com', '2016-01-01 08:15:32'),
('GenericUser3', 'GenericPassword3', 'generic3@gmail.com', '2016-01-02 10:47:19'),
('GenericUser4', 'GenericPassword4', 'generic4@gmail.com', '2016-01-02 16:03:54'),
('GenericUser5', 'GenericPassword5', 'generic5@gmail.com', '2016-01-03 09:27:11'),
('GenericUser6', 'GenericPassword6', 'generic6@gmail.com', '2016-01-03 21:45:06'),
('GenericUser7', 'GenericPassword7', 'generic7@gmail.com', '2016-01-04 14:12:43'),
('GenericUser8', 'GenericPassword8', 'generic8@gmail.com', '2016-01-05 07:55:01'),
('GenericUser9', 'GenericPassword9', 'generic9@gmail.com', '2016-01-06 13:20:22'),
('GenericUser10', 'GenericPassword10', 'generic10@gmail.com', '2016-01-07 17:38:45'),
('GenericUser11', 'GenericPassword11', 'generic11@gmail.com', '2016-01-08 11:06:30'),
('GenericUser12', 'GenericPassword12', 'generic12@gmail.com', '2016-01-09 22:51:17'),
('GenericUser13', 'GenericPassword13', 'generic13@gmail.com', '2016-01-10 15:34:58'),
('GenericUser14', 'GenericPassword14', 'generic14@gmail.com', '2016-01-11 06:10:29'),
('GenericUser15', 'GenericPassword15', 'generic15@gmail.com', '2016-01-12 12:00:00');
INSERT INTO `sf_db`.`subforums`
(`Name`, `Description`, `CreationDate`, `SubscriberCount`, `LastUpdated`, `OwnerID`)
VALUES
('Pokemon', 'Pls don''t sue Nintendo. We like Pokemon. (a lot). Come discuss about Pokemon, whether the video games, anime, the Pokemon themselves, or movies. We are always open to new community members wanting to get better at battling, team-building, and knowledge of the Pokemon in general (e.g. names, types, stat spreads, etc.', '2020-01-01 00:00:00', 2, '2020-01-01 00:00:00', 11),
#The subsequent subforums use AI-generated descriptions, and timestamps for demonstration. Using the prompt on ChatGPT 4/25/25 
#"generate a description for a subforum titled <Name>"
('Music', 'A place for all things melodic! Whether you''re a musician, listener, or simply a fan of all genres, this is where you can discuss your favorite artists, discover new sounds, share compositions, or explore music theory. From classical to electronic, rock to jazz, we celebrate the universal language of music here.', '2020-08-19 22:17:44', 2, '2020-08-19 22:17:44', 12),
('Coding', 'From your first "Hello, World!" to advanced algorithms—this is the place to talk code. Share projects, ask questions, debug with peers, and explore the world of programming across all languages and skill levels. Whether you''re a hobbyist, student, or professional developer, you''re welcome here.', '2023-01-29 07:59:31', 2, '2023-01-29 07:59:31', 8),
('Databases', 'Dive into the world of data! Discuss SQL and NoSQL databases, data modeling, performance tuning, backups, indexing, and everything in between. Whether you''re managing a massive enterprise system or tinkering with SQLite on a side project, this is your go-to hub for all things database-related.', '2020-01-01 00:00:00', 2, '2020-01-01 00:00:00', 5),
-- I wrote the Generic subforum
('Generic', 'Are You Generic?, Wellllll, I am too.', '2024-12-10 11:12:05', 2, '2024-12-10 11:12:05', 16),
('Flowers', 'A blooming space for floral enthusiasts! Share photos of your favorite blossoms, discuss gardening tips, learn about different species, and connect with fellow flower lovers. Whether you''re into wildflowers, roses, or rare orchids—this is the place to stop and smell them all.', '2018-05-26 14:30:00', 2, '2018-05-26 14:30:00', 1),
('MySQL', 'Everything MySQL in one place! Discuss database design, SQL queries, performance optimization, replication, troubleshooting, and best practices. Whether you''re just getting started or you''re a seasoned pro, this is the place to connect with other MySQL users, share tips, and solve challenges together.', '2021-03-14 05:14:33', 2, '2021-03-14 05:14:33', 6),
('PostgreSQL', 'Explore the power of PostgreSQL! Whether you''re managing complex data, optimizing queries, or setting up replication, this is the place to discuss everything PostgreSQL. Share tips on database design, performance tuning, troubleshooting, and leveraging advanced features like JSONB, indexing, and extensions. All PostgreSQL users—from beginners to experts—are welcome to join the conversation.', '2019-07-22 12:08:45', 2, '2019-07-22 12:08:45', 13),
('Java', 'Welcome to the world of Java! Whether you''re building web apps, mobile apps, or enterprise systems, this is the place to discuss Java programming, frameworks, libraries, and tools. Share code snippets, troubleshoot issues, and learn best practices for everything from object-oriented design to performance optimization.', '2024-11-03 17:52:10', 2, '2024-11-03 17:52:10', 9),
('Python', 'Join the Python community! Whether you''re just starting with Python or you''re an experienced developer, this is the place to discuss everything from basic syntax to advanced libraries and frameworks. Share code, ask questions, collaborate on projects, and explore topics like data science, web development, automation, and more!', '2023-02-18 00:35:20', 2, '2023-02-18 00:35:20', 8),
('C#', 'Welcome to the C# corner! Whether you''re developing desktop applications, web services, or games with Unity, this is the place to dive into everything C#. Share code snippets, debug together, explore .NET frameworks, and stay updated on the latest C# features and best practices. All skill levels are welcome!','2018-09-05 09:26:59', 2, '2018-09-05 09:26:59', 7),
('Classical Music', 'Step into the timeless world of classical music! Discuss your favorite composers, from Bach to Beethoven, Chopin to Tchaikovsky. Share insights on symphonies, concertos, operas, and chamber music. Whether you''re a long-time fan or new to the genre, this is the place to explore the rich history, theory, and beauty of classical compositions.', '2022-06-11 13:03:12', 2, '2022-06-11 13:03:12', 14),
('Jazz Music', 'Welcome to the smooth, soulful world of jazz! Whether you’re into bebop, swing, blues, or contemporary jazz, this is the place to discuss legendary artists, iconic albums, and the improvisational magic that makes jazz unique. Share your favorite tracks, learn about jazz theory, and connect with fellow enthusiasts who appreciate the rhythms, melodies, and innovations of this ever-evolving genre.', '2022-04-07 18:41:00', 2, '2022-04-07 18:41:00', 10),
('SQL', 'Welcome to the SQL subforum! Whether you''re mastering queries, optimizing performance, or tackling complex database problems, this is the place to discuss all things SQL. Share tips, ask questions, troubleshoot issues, and dive deep into relational database concepts. From basic SELECT statements to advanced joins, subqueries, and indexing, all SQL enthusiasts are welcome!', '2020-10-30 20:21:18', 2, '2020-10-30 20:21:18', 5),
('React', 'Welcome to the React subforum! Whether you''re building dynamic single-page applications, exploring hooks, or mastering state management, this is the place to discuss everything React. Share components, ask questions, troubleshoot issues, and discover the latest features and best practices in the React ecosystem. All React developers, from beginners to experts, are welcome to join the conversation!', '2022-03-03 03:03:03', 2, '2022-03-03 03:03:03', 3);
INSERT INTO `sf_db`.`posts` (`Title`, `BodyText`, `CreationDate`, `Rating`, `UserID`, `SubforumID`)
VALUES
('Mew vs. Mewtwo, Who Would Win?', 'Mew (Come @ Me)', '2020-01-01 00:00:00',  6, 11, 1),
('Mew vs. Mewtwo, Who Would Win?', 'Mewtwo (Come @ Me)', '2020-01-01 00:00:00',  10, 11, 1),
('Who''s Your Favorite Pokemon?', 'Mine is Serperior', '2020-01-01 00:00:00',  25, 11, 1),
('Why I Love Roses', 'Roses are so timeless, the perfect addition to any garden!', '2025-01-01 10:00:00', 5, 1, 5),
('Pokemon Battle Strategies', 'Trying a new team build for my next tournament!', '2025-01-01 11:30:00', 8, 2, 1),
('My Favorite Database Tool', 'I swear by MySQL for my projects. It''s efficient and simple.', '2024-12-01 09:00:00', 12, 5, 4),
('React is So Fun!', 'Learning React hooks has been a game-changer for my web apps.', '2025-02-01 14:45:00', 15, 3, 15),
('C# for Unity Development', 'I love using C# with Unity to create smooth gameplay mechanics.', '2024-05-15 12:20:00', 10, 7, 11),
('Debugging in Python', 'Spent hours trying to fix a bug. Python’s traceback really helps!', '2024-03-20 16:00:00', 20, 8, 10),
('MySQL vs PostgreSQL', 'I like PostgreSQL better because of its JSONB features.', '2023-12-10 08:30:00', 5, 4, 14),
('Gaming with Java', 'Is anyone here making games with Java? Would love to collaborate!', '2025-01-10 18:25:00', 25, 9, 9),
('The Magic of Jazz Music', 'Jazz just speaks to me. There''s something magical about it.', '2025-01-15 20:00:00', 18, 10, 15),
('Learning Data Structures in C#', 'Trying to implement a binary search tree in C# today.', '2024-10-05 17:15:00', 10, 11, 11),
('PostgreSQL Indexing', 'Have you guys optimized your queries using PostgreSQL indexing?', '2024-06-03 13:30:00', 22, 13, 8),
('Flowers and Gardening Tips', 'This spring, I''m trying out hydrangeas. Any tips on care?', '2025-01-02 10:00:00', 14, 12, 7),
('Exploring React Redux', 'Started experimenting with Redux for state management in React.', '2025-01-01 12:45:00', 10, 6, 15),
('C# Exception Handling', 'Learning how to handle exceptions in C#. Any tips?', '2024-08-19 08:00:00', 10, 7, 11),
('SQL Query Optimization', 'Anyone know how to optimize complex JOIN queries in SQL?', '2024-09-23 11:15:00', 20, 14, 14);

INSERT INTO `sf_db`.`comments`
(`CommentText`, `CreationDate`, `Rating`, `UserID`, `PostID`, `ParentID`, `LastUpdated`)
VALUES
('Lovely Day Today, Mewtwo would win no cap! They got higher stats!', '2020-01-01 00:00:00', 10, 10, 1, 1, '2020-01-01 00:00:00'),
-- Generated Subsequent Comments ChatGPT 4/25/25 
('Mew is adorable but I think Mewtwo would dominate.', '2025-01-01 08:00:00', 4, 2, 1, 2, '2025-01-01 08:00:00'),
-- Nested reply to CommentID 2
('Maybe, but Mew has more flexibility in movesets.', '2025-01-01 08:10:00', 5, 3, 1, 2, '2025-01-01 08:10:00'),
-- Nested reply to CommentID 3
('I mean, versatility is great, but sheer power often wins.', '2025-01-01 08:20:00', 2, 5, 1, 3, '2025-01-01 08:20:00'),
-- Another top-level comment
('Honestly I prefer Lucario over both of them.', '2025-01-01 08:30:00', 3, 6, 1, 5, '2025-01-01 08:30:00'),
-- Reply to CommentID 5
('Lucario is cool, but c''mon—legendary status matters!', '2025-01-01 08:40:00', 4, 8, 1, 5, '2025-01-01 08:40:00'),
-- Deep reply to CommentID 6
('Fair, but Lucario had that movie. Instant classic.', '2025-01-01 08:50:00', 6, 10, 1, 6, '2025-01-01 08:50:00'),
-- Another top-level comment
('I raised a Mew from level 5—pure nostalgia.', '2025-01-01 09:00:00', 7, 11, 1, 8, '2025-01-01 09:00:00'),
-- Reply to CommentID 8
('Same! Got mine from that old Toys R Us event.', '2025-01-01 09:10:00', 5, 13, 1, 8, '2025-01-01 09:10:00'),
-- PostID = 2
('Mewtwo is so OP. Anyone else remember the Gen 1 days?', '2020-01-01 08:15:00', 10, 2, 2, 10, '2020-01-01 08:15:00'),
-- PostID = 3
('Serperior is stylish, but I gotta go with Infernape.', '2020-01-01 08:17:00', 5, 3, 3, 11, '2020-01-01 08:17:00'),
-- PostID = 4
('Totally agree! Roses add elegance to any space.', '2025-01-01 11:00:00', 8, 4, 4, 12, '2025-01-01 11:00:00'),
-- PostID = 5
('Rain team meta is super effective rn. Try Pelipper + Barraskewda!', '2025-01-01 11:45:00', 9, 5, 5, 13, '2025-01-01 11:45:00'),
-- PostID = 6
('MySQL is reliable. Been using it for years with Laravel.', '2024-12-01 09:15:00', 7, 6, 6, 14, '2024-12-01 09:15:00'),
-- PostID = 7
('React hooks made my life so much easier. useEffect = ❤️', '2025-02-01 15:00:00', 10, 7, 7, 15, '2025-02-01 15:00:00'),
-- PostID = 8
('Unity and C# go together like PB&J. Smooth and powerful.', '2024-05-15 12:30:00', 9, 8, 8, 16, '2024-05-15 12:30:00'),
-- PostID = 9
('Tracebacks have saved me so many times. Thank you, Python devs.', '2024-03-20 16:15:00', 11, 9, 9, 17, '2024-03-20 16:15:00'),
-- PostID = 10
('Postgres ftw. JSONB support is 🔥🔥🔥.', '2023-12-10 09:00:00', 6, 10, 10, 18, '2023-12-10 09:00:00'),
-- PostID = 11
('I dabble in Java game dev. Would love to team up!', '2025-01-10 18:45:00', 13, 11, 11, 19, '2025-01-10 18:45:00'),
-- PostID = 12
('Nothing like jazz to wind down after work.', '2025-01-15 20:30:00', 14, 12, 12, 20, '2025-01-15 20:30:00'),
-- PostID = 13
('Try a recursive approach. Also check for null nodes!', '2024-10-05 17:30:00', 5, 13, 13, 21, '2024-10-05 17:30:00'),
-- PostID = 14
('Indexing cut my query time by 80%. Highly recommend btree index.', '2024-06-03 13:45:00', 7, 14, 14, 22, '2024-06-03 13:45:00'),
-- PostID = 15
('Hydrangeas like acidic soil. Test your pH levels!', '2025-01-02 10:30:00', 9, 15, 15, 23, '2025-01-02 10:30:00'),
-- PostID = 16
('Redux was confusing at first, but it’s great once it clicks.', '2025-01-01 13:00:00', 8, 6, 16, 24, '2025-01-01 13:00:00'),
-- PostID = 17
('Use try-catch-finally for control flow. Don’t forget logging!', '2024-08-19 08:15:00', 7, 7, 17, 25, '2024-08-19 08:15:00'),
-- CommentID = 26
('Totally feel this. useSelector tripped me up at first.', '2025-01-01 13:10:00', 6, 7, 16, 24, '2025-01-01 13:10:00'),
-- CommentID = 27
('Once I grasped the store concept, everything started to make sense.', '2025-01-01 13:12:00', 5, 8, 16, 24, '2025-01-01 13:12:00'),
-- CommentID = 28
('Pro tip: use Redux Toolkit. Makes setup *so* much easier.', '2025-01-01 13:15:00', 10, 9, 16, 24, '2025-01-01 13:15:00'),
-- CommentID = 29
('Yeah, `createSlice` + `configureStore` simplified everything for me.', '2025-01-01 13:18:00', 8, 10, 16, 24, '2025-01-01 13:18:00'),
-- CommentID = 30
('Also: combine Redux DevTools with React DevTools = mind blown!', '2025-01-01 13:22:00', 9, 11, 16, 24, '2025-01-01 13:22:00');
INSERT INTO `sf_db`.`subscriptions`
(`UserID`, `SubforumID`, `SubscriptionDate`)
VALUES
-- Flowers-6
(1, 6, '2019-01-01 13:22:00'),
(2, 6, '2019-01-01 13:22:00'),
(13, 6, '2019-01-01 13:22:00'),
(14, 6, '2019-01-01 13:22:00'),
-- Pokemon-1
(3, 1, '2019-01-01 13:22:00'),
(4, 1, '2019-01-01 13:22:00'),
(8, 1, '2019-01-01 13:22:00'),
(9, 1, '2019-01-01 13:22:00'),
(15, 1, '2019-01-01 13:22:00'),
-- Music-2, 12, 13
(2, 2, '2019-01-01 13:22:00'),
(2, 12, '2019-01-01 13:22:00'),
(2, 13, '2019-01-01 13:22:00'),
(12, 2, '2019-01-01 13:22:00'),
(12, 12, '2019-01-01 13:22:00'),
(12, 13, '2019-01-01 13:22:00'),
(13, 2, '2019-01-01 13:22:00'),
(13, 12, '2019-01-01 13:22:00'),
(13, 13, '2019-01-01 13:22:00'),
(14, 2, '2019-01-01 13:22:00'),
(14, 12, '2019-01-01 13:22:00'),
(14, 13, '2019-01-01 13:22:00'),
-- Databases- 7, 8, 14 Users-5, 6, 
(5, 7, '2019-01-01 13:22:00'),
(5, 8, '2019-01-01 13:22:00'),
(5, 14, '2019-01-01 13:22:00'),
(6, 7, '2019-01-01 13:22:00'),
(6, 8, '2019-01-01 13:22:00'),
(6, 14, '2019-01-01 13:22:00'),
-- Coding- 9, 10, 11 Users-7, 8, 9
(7, 9, '2019-01-01 13:22:00'),
(7, 10, '2019-01-01 13:22:00'),
(7, 11, '2019-01-01 13:22:00'),
(8, 9, '2019-01-01 13:22:00'),
(8, 10, '2019-01-01 13:22:00'),
(8, 11, '2019-01-01 13:22:00'),
(9, 9, '2019-01-01 13:22:00'),
(9, 10, '2019-01-01 13:22:00'),
(9, 11, '2019-01-01 13:22:00'),
-- Generic- 5 Users- 16-30
(16, 5, '2019-01-01 13:22:00'),
(17, 5, '2019-01-01 13:22:00'),
(18, 5, '2019-01-01 13:22:00'),
(19, 5, '2019-01-01 13:22:00'),
(20, 5, '2019-01-01 13:22:00'),
(21, 5, '2019-01-01 13:22:00'),
(22, 5, '2019-01-01 13:22:00'),
(23, 5, '2019-01-01 13:22:00'),
(24, 5, '2019-01-01 13:22:00'),
(25, 5, '2019-01-01 13:22:00'),
(26, 5, '2019-01-01 13:22:00'),
(27, 5, '2019-01-01 13:22:00'),
(28, 5, '2019-01-01 13:22:00'),
(29, 5, '2019-01-01 13:22:00'),
(30, 5, '2019-01-01 13:22:00');


SELECT * FROM users;
SELECT * FROM subforums;
SELECT * FROM posts;
SELECT * FROM comments;
SELECT * FROM subscriptions;