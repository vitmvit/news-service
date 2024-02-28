INSERT INTO "news" ("id", "time", "text", "title")
VALUES (1, '2024-02-17 16:31:38.166727', 'text', 'titleOne'),
       (2, '2024-02-17 16:31:39.772884', 'text', 'titleTwo'),
       (3, '2024-02-17 16:31:40.477176', 'text', 'titleThree'),
       (4, '2024-02-17 16:31:41.232163', 'text', 'titleFour');

ALTER TABLE "news" DROP CONSTRAINT news_pkey;