SELECT * FROM item



-- Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst? 
-- Hinweis: Geben Sie das Ergebnis in einer 3-spaltigen Relation aus.

SELECT * FROM 
	(SELECT COUNT (book_id) num_books FROM bookspec),
	(SELECT COUNT (cd_id) num_cds FROM musicspec),
	(SELECT COUNT (dvd_id) num_dvds FROM dvdspec)
	;

