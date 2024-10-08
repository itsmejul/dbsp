SELECT * FROM item i WHERE pgroup = 'Book' AND NOT EXISTS (SELECT * FROM bookspec b WHERE b.asin = i.asin)
-- ANZAHL AN BOOKS IN ITEM UND IN BOOKSPEC VERSCHIEDEN

-- ANFRAGE 1--------------------------------------------------------------------------------------------------
-- Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst? 
-- Hinweis: Geben Sie das Ergebnis in einer 3-spaltigen Relation aus.
DROP TABLE IF EXISTS query_1;
CREATE TABLE query_1 AS
SELECT * FROM 
	(SELECT COUNT (book_id) num_books FROM bookspec),
	(SELECT COUNT (cd_id) num_cds FROM musicspec),
	(SELECT COUNT (dvd_id) num_dvds FROM dvdspec)
	;
SELECT * FROM query_1;

-- ANFRAGE 2---------------------------------------------------------------------------------------------
-- Nennen Sie die 5 besten Produkte jedes Typs (Buch, Musik-CD, DVD) sortiert nach dem durchschnittlichem Rating. 
-- Hinweis: Geben Sie das Ergebnis in einer einzigen Relation mit den Attributen Typ, ProduktNr, Rating aus. 
-- Wie werden gleiche durchschnittliche Ratings behandelt?

-- nimmt aus der hilfsmethode immer die 5 mit dem höchsten rating pro type
DROP TABLE IF EXISTS query_2;
CREATE TABLE query_2 AS
SELECT
    asin, average_rating, pgroup
FROM
	(SELECT *, ROW_NUMBER() OVER (PARTITION BY pgroup ORDER BY average_rating DESC) AS row_num
	FROM
        	(SELECT * FROM
			(SELECT asin, AVG(rating) average_rating FROM product_reviews r GROUP BY asin) NATURAL JOIN (SELECT asin, pgroup FROM item)
		)
    	)
WHERE row_num <= 5
ORDER BY pgroup, asin; -- Nimm nur die 5 ersten Ergebnisse pro Typ, also ersten 5 row_number
SELECT * FROM query_2;

	
-- ANFRAGE 3--------------------------------------------------------------------------------------------------------------
-- Für welche Produkte gibt es im Moment kein Angebot?
-- die anfrage sollte gehen
DROP TABLE IF EXISTS query_3;
CREATE TABLE query_3 AS
SELECT DISTINCT i.asin, i.title  FROM item i NATURAL JOIN price p WHERE NOT EXISTS (SELECT * FROM price p WHERE i.asin = p.asin AND p.price_value IS NOT NULL)
ORDER BY i.title;
SELECT * FROM query_3;
-- ANFRAGE 4---------------------------------------------------------------------------------------------------------------
	-- bevor die items ohne spec gelöscht wurden, waren es 4 ergebnisse
DROP TABLE IF EXISTS query_4;
CREATE TABLE query_4 AS
SELECT p1.asin, p1.price_value AS Preis_1, p2.price_value AS Preis_2 FROM price p1, price p2 WHERE p1.asin = p2.asin AND p1.price_value > 2 * p2.price_value;
SELECT * FROM query_4;

--SELECT * FROM price WHERE asin = 'B00004CWTY'
-- ANFRAGE 5
DROP TABLE IF EXISTS query_5;
CREATE TABLE query_5 AS
SELECT DISTINCT r1.asin FROM product_reviews r1, product_reviews r2 WHERE r1.asin = r2.asin AND r1.rating = 1 AND r2.rating = 5;
SELECT * FROM query_5;
-- ANFRAGE 6
-- FÜR WIE VIELE PRODUKTE GIBT ES GAR KEINE REZENSIONEN
DROP TABLE IF EXISTS query_6;
CREATE TABLE query_6 AS
SELECT COUNT(asin) as number_without_reviews FROM (
SELECT * FROM item i WHERE NOT EXISTS (SELECT * FROM product_reviews r WHERE r.asin = i.asin));
SELECT * FROM query_6;

-- ANFRAGE 7
	DROP TABLE IF EXISTS query_7;
CREATE TABLE query_7 AS
SELECT  c.username, r.number_of_reviews FROM (
SELECT DISTINCT customer_id, COUNT(asin) number_of_reviews FROM product_reviews 
GROUP BY customer_id having COUNT(asin) > 9) r NATURAL JOIN customer c;
SELECT * FROM query_7;



--ANFRAGE 8
DROP TABLE IF EXISTS query_8;
CREATE TABLE query_8 AS
SELECT DISTINCT a.author_name FROM author a WHERE EXISTS (
	(SELECT director_name AS person_name FROM director d, item i WHERE d.director_name = a.author_name AND d.asin = i.asin AND i.pgroup <> 'Book') --Alle die auch DIRECTOR sind
UNION	(SELECT creator_name AS person_name FROM creator d, item i WHERE d.creator_name = a.author_name AND d.asin = i.asin AND i.pgroup <> 'Book') --Alle die auch Creator sind
UNION (SELECT actor_name AS person_name FROM actor d, item i WHERE d.actor_name = a.author_name AND d.asin = i.asin AND i.pgroup <> 'Book')  -- Alle die auch ACTOR sind
UNION (SELECT artist_name AS person_name FROM artist d, item i WHERE d.artist_name = a.author_name AND d.asin = i.asin AND i.pgroup <> 'Book')); -- Alle die auch ARTIST sind
SELECT * FROM query_8;

SELECT * FROM author WHERE author_name = 'Wolfgang Amadeus Mozart'
SELECT * FROM ITEM WHERE asin = 'B0006OL5E0'  -- nochmal anschauen
SELECT * FROM bookspec WHERE asin = 'B0006OL5E0'
--ANFRAGE 9
DROP TABLE IF EXISTS query_9;
CREATE TABLE query_9 AS
SELECT AVG(number_of_tracks) FROM (
SELECT asin, COUNT(track_title) AS number_of_tracks FROM tracks GROUP BY asin);
SELECT * FROM query_9; --die ohne tracks rauslöshen??? machen wir das

-- ANFRAGE 10
-- Hauptcategorien für jedes item bestimmen
-- erst alle kategorien auf ihre hauptkategorie mappen
DROP TABLE IF EXISTS query_10;
CREATE TABLE query_10 AS
WITH RECURSIVE category_hierarchy AS (
    -- Standardfall: alle hauptkategorien
    SELECT 
        id AS category_id,
        title AS top_category_title,
        id AS top_category_id
    FROM 
        categories
    WHERE 
        parent_id IS NULL
    
    UNION ALL
    -- Rekursiver fall: subkategorien hinzufügen
    
    SELECT 
        c.id AS category_id,
        ch.top_category_title,
        ch.top_category_id
    FROM 
        categories c
     JOIN -- inner
        category_hierarchy ch ON c.parent_id = ch.category_id
),
-- dann alle items auf ihre hauptkategorie mappen
items_with_top_categories AS (
	
    -- JOINE item_categories mit category_hierarchy also den dem mapping von kategorien auf ihre hauptkategorie
    SELECT 
        ic.asin,
        ch.top_category_id,
        ch.top_category_title
    FROM 
        item_categories ic
    JOIN --inner
        category_hierarchy ch ON ic.category_id = ch.category_id
) ,
-- alle similar produkts mit den hauptkategorien ihrer beiden items
similar_products_with_categories AS (
    
    SELECT 
        sp.asin_original,
        sp.asin_similar,
        iwc1.top_category_id AS original_top_category_id,
        iwc1.top_category_title AS original_top_category_title,
        iwc2.top_category_id AS similar_top_category_id,
        iwc2.top_category_title AS similar_top_category_title
    FROM 
        sim_products sp
    JOIN --inner 
        items_with_top_categories iwc1 ON sp.asin_original = iwc1.asin
    JOIN --inner 
        items_with_top_categories iwc2 ON sp.asin_similar = iwc2.asin
),
-- Liste von allen similar produkts die in verschiedenen hauptkategorien sind
items_with_similar_in_different_top_category AS (

SELECT DISTINCT
    asin_original,
    asin_similar,
    original_top_category_id,
    original_top_category_title,
    similar_top_category_id,
    similar_top_category_title
FROM 
    similar_products_with_categories
WHERE 
    original_top_category_id <> similar_top_category_id
ORDER BY 
    asin_original, asin_similar
)
-- erforderte Ausgabe: Asins von allen Items, die ähnliche Items in anderer Hauptkategorie haben
SELECT DISTINCT asin_original FROM items_with_similar_in_different_top_category;
SELECT * FROM query_10;



-- ANFRAGE 11 ---------------------------------------------------------------------------
--Produkte die in allen Filialen angeboten werden
DROP TABLE IF EXISTS query_11;
CREATE TABLE query_11 AS
SELECT DISTINCT i.asin, i.title FROM item i WHERE NOT EXISTS             -- all items, wo kein shop existiert, der die bedingung erfüllt:
    (SELECT shop_id FROM shops s WHERE NOT EXISTS    -- bedingung: das item wird in dem shop nicht angeboten (es existiert kein angebot mit dem shop und dem preis zusammen)
   		(SELECT * FROM price p WHERE p.asin = i.asin AND p.price_shop_id = s.shop_id)) 
ORDER BY i.title;
SELECT * FROM query_11;

-- ANFRAGE 12---------------------------------------------------------------------------------
-- in wieviel prozent der fälle von anfrage 11 ist das billigste angebot in leipzig
-- was ist wenn beide gleiches angeboten haben?
DROP TABLE IF EXISTS query_12;
CREATE TABLE query_12 AS
WITH items_in_all_shops AS (
	
SELECT * FROM item i WHERE NOT EXISTS             -- all items, wo kein shop existiert, der die bedingung erfüllt:
    (SELECT shop_id FROM shops s WHERE NOT EXISTS    -- bedingung: das item wird in dem shop nicht angeboten (es existiert kein angebot mit dem shop und dem preis zusammen)
   		(SELECT * FROM price p WHERE p.asin = i.asin AND p.price_shop_id = s.shop_id)) 
),
number_leipzig AS
(
SELECT COUNT (*) AS num1 FROM (       --125 items haben in lpz kleinsten preis, 85 in dresden. restliche 93 haben bei beiden keinen preis
	SELECT p1.price_shop_id, p1.asin FROM price p1, items_in_all_shops i WHERE p1.price_value = (  -- finde für jedes item den shop mit dem kleisten preis
         SELECT MIN(price_value) FROM price p WHERE p.asin = i.asin) -- finde für jedes item den kleisten preis
) WHERE price_shop_id =( SELECT shop_id FROM shops WHERE shop_name = 'Leipzig'  ) --noch ändern sodass nach name gesucht wird mit with ...as select für die id
),
number_total AS
(
SELECT COUNT(*) AS num2 FROM items_in_all_shops -- Anzahl items die in allen shops sind
) 
SELECT CAST(nl.num1 AS float) / CAST(nt.num2 AS float) AS prozentsatz FROM number_leipzig nl, number_total nt;
SELECT * FROM query_12;








SELECT * FROM item WHERE avg_review_score IS NOT NULL

-- Testanfragen zum löschen von Nutzern
SELECT * FROM customer
SELECT * FROM customer WHERE customer_id = 1
SELECT * FROM product_reviews WHERE customer_id = 1
DELETE FROM customer WHERE customer_id = 1
SELECT * FROM product_reviews WHERE review_id = 1570

