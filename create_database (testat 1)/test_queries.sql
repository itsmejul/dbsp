---------RETURN ALL ITEMS WITH MULTIPLE PRICES
SELECT *
FROM price p1, price p2
WHERE p1.item_id = p2.item_id AND p1.price_id <> p2.price_id

------------test für ein beispiel was in beiden shops verschiedene preise hat
SELECT *
FROM price p
WHERE p.item_id = 2317
--
SELECT * 
FROM item
WHERE asin = 'B00005UW4X'
SELECT *
FROM artist



SELECT *
FROM musicspec

SELECT i.title
FROM item i
WHERE i.item_id in (SELECT item_id2 FROM sim_products WHERE item_id = 2)


--ALLE ITEMS DIE ALS SIM_PRODUCT GELISTET SIND ABER NICHT ALS ECHTES ITEM EXISTIEREN (GAR KEINE)
SELECT s.* 
FROM sim_products s
WHERE NOT EXISTS 
(SELECT i.* FROM item i WHERE s.asin = i.asin )


-- ALLE SIMILAR-BEZIEHUNGEN DIE ES NICHT IN DIE ANDERE RICHTUNG GIBT (ALSO IST ES NICHT SYMMETRISCH)
SELECT s1.*
FROM sim_products s1
WHERE NOT EXISTS 
(SELECT * FROM sim_products s2 
WHERE (SELECT asin FROM item i WHERE i.item_id = s1.item_id) = s2.asin 
AND s1.asin = (SELECT asin FROM item i WHERE i.item_id = s2.item_id))


--ALLE ITEMS DIE SOWOHL IN LEIPZIG ALS AUCH DRESDEN SIND
SELECT i1.item_id, i2.item_id
FROM item i1, item i2
WHERE i1.asin = i2.asin AND i1.item_id <> i2.item_id AND i1.shop_id <> i2.shop_id
--ALLE ITEMS WO IM GLEICHEN SHOP ZWEI DIE GLEICHE ASIN HABEN (GAR KEINE)
SELECT * 
FROM item i1, item i2
WHERE i1.asin = i2.asin AND i1.item_id <> i2.item_id 
AND i1.shop_id = i2.shop_id


SELECT * FROM item
--ALLE ITEMS WO ZWEI DIE GLEICHE ASIN HABEN ABER SICH IN ANDEREN ATTRIBUTEN UNTERSCHEIDEN DIE NICHT DER TITEL SIND, weil titel oft anders weil äöü fehlen in dresden
SELECT asin
FROM item
GROUP BY asin
HAVING COUNT(DISTINCT ( pgroup || ean || picture || detailpage)) > 1;

--Alle mit fehlerhafter pgroup
SELECT * 
FROM item
WHERE pgroup <> 'Book' AND pgroup <> 'Music' AND pgroup <> 'DVD'

--ALLE PREISE WO FÜR DAS GLEICHE PRODUKT SIND
SELECT *
FROM price p1, price p2
WHERE (SELECT asin FROM item i WHERE i.item_id = p1.item_id ) = (SELECT asin FROM item i WHERE i.item_id = p2.item_id) AND p1.price_id <> p2.price_id
AND p1.price_value <> p2.price_value

SELECT * FROM price p1, price p2
WHERE p1.item_id = p2.item_id AND p1.price_id <> p2.price_id AND (p1.price_value IS NULL OR p1.price_value IS NOT NULL)

--QUERY DIE ALLE PREIS DUPLIKATE RETURNT. ABER DA DIE DOPPELTEN PREISE IMMER VERSCHIEDENE SHOP_ID HABEN GIBT ES KEINE
SELECT item_id, price_state, price_mult, price_currency, price_value, price_shop_id, COUNT(*)
FROM price
GROUP BY item_id, price_state, price_mult, price_currency, price_value, price_shop_id
HAVING COUNT(*) > 1;