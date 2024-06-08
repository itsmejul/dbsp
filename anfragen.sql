SELECT * FROM item



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

-- Hilfsmethode, gibt alle average ratings für alle items
SELECT * FROM
(SELECT asin, AVG(rating) average_rating FROM product_reviews r GROUP BY asin) NATURAL JOIN (SELECT asin, pgroup FROM item)
ORDER BY pgroup, average_rating DESC


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