-- Testprogramm für die dynamische Berechnung von avg_review_score
-- Alle Schritte sind einzeln auszuführen
-- Die create_table.sql und triggers.sql müssen vorher ausgeführt werden

-- 1. Erstelle neues Item und teste Konsistenz von avg_review_score
INSERT INTO item (asin, title, ean)
VALUES	('123451test', 'TESTITEM FÜR TRIGGERS', '1231234512345');
SELECT * FROM ITEM WHERE asin = '123451test';
-- avg_review_score = NULL


-- 2. Erstelle review mit rating 5
INSERT INTO product_reviews (asin, rating, reviewdate)
VALUES ('123451test', 5, '2024-05-14');
SELECT * FROM ITEM WHERE asin = '123451test';
-- avg_review_score = 5


-- 3. Erstelle review mit rating 1
INSERT INTO product_reviews (asin, rating, reviewdate)
VALUES ('123451test', 1, '2024-05-14');
SELECT * FROM ITEM WHERE asin = '123451test';
-- avg_review_score = 3


-- 4. Wähle review_id aus, die bearbeitet oder gelöscht werden soll
 SELECT * FROM product_reviews WHERE asin = '123451test';


-- 5. Update review, zB ändere rating von 1 zu 2
UPDATE product_reviews 
  SET rating = 2
  WHERE review_id = '6333';   --hier die review_id der zu updatenden review eingeben
SELECT * FROM ITEM WHERE asin = '123451test';


-- 6. Lösche review
DELETE FROM product_reviews WHERE review_id = '6333'; -- hier die review_id der zu löschenden review eingeben
SELECT * FROM ITEM WHERE asin = '123451test';





-- zusätzliches Test-Statement, das beim inserten die neue review_id zurückgibt
WITH new_review AS 
(INSERT INTO product_reviews (asin, rating, reviewdate)
VALUES ('123451test', 1, '2024-05-14')
returning review_id
)
SELECT review_id FROM new_review;

