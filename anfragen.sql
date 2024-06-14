SELECT * FROM item i WHERE pgroup = 'Book' AND NOT EXISTS (SELECT * FROM bookspec b WHERE b.asin = i.asin)
-- ANZAHL AN BOOKS IN ITEM UND IN BOOKSPEC VERSCHIEDEN


-- Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst? 
-- Hinweis: Geben Sie das Ergebnis in einer 3-spaltigen Relation aus.

SELECT * FROM 
	(SELECT COUNT (book_id) num_books FROM bookspec),
	(SELECT COUNT (cd_id) num_cds FROM musicspec),
	(SELECT COUNT (dvd_id) num_dvds FROM dvdspec)
	;

-- Nennen Sie die 5 besten Produkte jedes Typs (Buch, Musik-CD, DVD) sortiert nach dem durchschnittlichem Rating. 
-- Hinweis: Geben Sie das Ergebnis in einer einzigen Relation mit den Attributen Typ, ProduktNr, Rating aus. 
-- Wie werden gleiche durchschnittliche Ratings behandelt?

-- nimmt aus der hilfsmethode immer die 5 mit dem höchsten rating pro type
SELECT
    *
FROM
    (SELECT
        *,
        ROW_NUMBER() OVER (PARTITION BY pgroup ORDER BY average_rating DESC) AS row_num -- row_number zählt die zeilen
    FROM
        (SELECT * FROM
			(SELECT asin, AVG(rating) average_rating FROM product_reviews r GROUP BY asin) NATURAL JOIN (SELECT asin, pgroup FROM item)
		)
)
WHERE
    row_num <= 5; -- Nimm nur die 5 ersten Ergebnisse pro Typ, also ersten 5 row_number


Select distinct asin FROM item
-- Für welche Produkte gibt es im Moment kein Angebot? -- geht noch nicht, anzahlen sind iwie komisch insgesamt
SELECT DISTINCT asin FROM price i WHERE NOT EXISTS (SELECT * FROM price p WHERE p.asin = i.asin AND p.price_value IS NOT NULL)