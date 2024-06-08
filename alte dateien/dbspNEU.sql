-- read data from dresden into xmldatadresden table, split by items
DROP TABLE IF EXISTS xmldatadresden CASCADE;
CREATE TABLE xmldatadresden (
 item xml);
INSERT INTO xmldatadresden (item)
SELECT unnest(xpath('//item[not(parent::similars)]', XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:\temp\dresden.xml'), 'UTF8')::xml)));
		
--READ DATA FROM LEIPZIG INTO TEMPORARY TABLE,SPLIT BY ITEMS
DROP TABLE IF EXISTS xmldataleipzig CASCADE;
CREATE TABLE xmldataleipzig (
 item xml);
INSERT INTO xmldataleipzig (item)
SELECT unnest(xpath('//item', XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:\temp\leipzig_transformed.xml'), 'UTF8')::xml)));
	
	
-----------------------------------SHOPS-----------------------------------------------
--ERSTELLE TABELLE FÜR SHOPS
DROP TABLE IF EXISTS shops CASCADE;
CREATE TABLE shops (
shop_id SERIAL PRIMARY KEY,
	shop_name text,
	shop_street text,
	shop_zip text,
	xmldata xml
);
--FÜGE SHOPS AUS LEIPZIG UND DRESDEN IN XML IN TABELLE EIN
INSERT INTO shops (xmldata)
SELECT unnest(xpath('//shop', XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:\temp\leipzig_transformed.xml'), 'UTF8')::xml)));
INSERT INTO shops (xmldata)
SELECT unnest(xpath('//shop', XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:\temp\dresden.xml'), 'UTF8')::xml)));
--EXTRAHIERE ATTRIBUTE AUS SHOPS XML
UPDATE shops
SET
  shop_name = (xpath('//shop/@name', xmldata))[1]::text,
  shop_street = (xpath('//shop/@street', xmldata))[1]::text,
  shop_zip = (xpath('//shop/@zip', xmldata))[1]::text;
--ENTFERNE HILFSSPALTE MIT XML
ALTER TABLE shops DROP COLUMN xmldata;



------------------------------------ITEM-------------------------------------------------------------
--ERSTELLE TABELLE ITEM
DROP TABLE IF EXISTS item CASCADE;
CREATE TABLE item (
item_id SERIAL PRIMARY KEY, 
title text,
pgroup text,
shop_id integer,
salesrank text,
asin text,
ean text,
picture text,
detailpage text,
xmldata xml);
--DATEN AUS LEIPZIG IN ITEM EINFÜGEN
INSERT INTO item (title, pgroup, shop_id, salesrank, asin, ean, picture, detailpage, xmldata)
SELECT 
    (xpath('/item/title/text()', item))[1]::text AS title,
    (xpath('/item/@pgroup', item))[1]::text AS pgroup,
	1 AS shop_id,
	(xpath('/item/@salesrank', item))[1]::text AS salesrank,
	(xpath('/item/@asin', item))[1]::text AS asin,
	(xpath('/item/@ean', item))[1]::text AS ean,
	(xpath('/item/@picture', item))[1]::text AS picture,
		(xpath('/item/@detailpage', item))[1]::text AS detailpage,
	item
FROM xmldataleipzig;
--DATEN AUS DRESDEN IN ITEM EINFÜGEN
INSERT INTO item (title, pgroup, shop_id, salesrank, asin, ean, picture, detailpage, xmldata)
SELECT 
    (xpath('/item/title/text()', item))[1]::text AS title,
    (xpath('/item/@pgroup', item))[1]::text AS pgroup,
	 2 AS shop_id,
	(xpath('/item/@salesrank', item))[1]::text AS salesrank,
	(xpath('/item/@asin', item))[1]::text AS asin,
	(xpath('/item/ean/text()', item))[1]::text AS ean,
	(xpath('/item/details/@img', item))[1]::text AS picture,
		(xpath('/item/details/text()', item))[1]::text AS detailpage,
	item
FROM xmldatadresden;



--------------------------------------PRICE---------------------------------------------------------------------
--PRICE ALS EXTRA RELATION, DA EIN ITEM MEHRERE PREISE HABEN KANN (zB NEU UND SECOND HAND)
DROP TABLE IF EXISTS price;
CREATE TABLE price(
price_id SERIAL PRIMARY KEY,
item_id integer,
price_state text,
price_mult text,
price_currency text,
price_value text,
price_shop_id text,
FOREIGN KEY (item_id) REFERENCES item(item_id)
);
--PREISE AUS ITEMS IN PRICE TABELLE EINFÜGEN
INSERT INTO PRICE (item_id, price_state, price_mult, price_currency, price_value , price_shop_id) 
SELECT item_id, 
 unnest(xpath('//price/@state', xmldata::xml))::text AS price_state,
 unnest(xpath('//price/@mult', xmldata::xml))::text AS price_mult, 
 unnest(xpath('//price/@currency', xmldata::xml))::text AS price_currency,
 unnest(xpath('//price/text()', xmldata::xml))::text AS price_value,
 shop_id
FROM item;

--------------------------------LÖSCHE DOPPELTE ITEMS (gleiche asin) DUPLIKATE RAUS ABER SPEICHERE BEIDE PREISE----------------
-- zwischenspeichern von duplikaten und jeweils eine id wird behalten
DROP TABLE IF EXISTS duplicate_items;
CREATE TEMP TABLE duplicate_items AS
WITH DuplicateItems AS (
    SELECT 
        asin, -- anhand von asin werden duplikate ermittelt, da in jedem shop die asin unique ist
        MIN(item_id) AS item_id_to_keep,
        ARRAY_AGG(item_id) AS duplicate_item_ids
    FROM 
        item
    GROUP BY 
        asin 
    HAVING 
        COUNT(*) > 1
)
SELECT
    asin, 
    item_id_to_keep,
    UNNEST(duplicate_item_ids) AS duplicate_item_id
FROM 
    DuplicateItems;
	
-- Update price tabelle, um zu den verbliebenen item_ids zu linken
UPDATE 
    price p
SET 
    item_id = di.item_id_to_keep
FROM 
    duplicate_items di
WHERE 
    p.item_id = di.duplicate_item_id
AND 
    p.item_id <> di.item_id_to_keep;
	
-- duplikate können jetzt gelöscht werden
DELETE FROM 
    item 
USING 
    duplicate_items di
WHERE 
    item.item_id = di.duplicate_item_id
AND 
    item.item_id <> di.item_id_to_keep;



-------------------------------------LISTS-----------------------------------------------
--ERSTELLE TABELLE FÜR LISTEN
DROP TABLE IF EXISTS lists;
CREATE TABLE lists(
	list_id SERIAL PRIMARY KEY,
	item_id integer,
	listname text,
FOREIGN KEY (item_id) REFERENCES item(item_id));
--FÜGE LISTMANIA LISTEN VON LEIPZIG EIN
INSERT INTO lists (item_id, listname)	
SELECT item_id, unnest(xpath('//list/@name', xmldata::xml))::text AS listname 
FROM item WHERE item.shop_id = 1;
--FÜGE LISTMANIA LISTEN VON DRESDEN EIN
INSERT INTO lists (item_id, listname)	
SELECT item_id, unnest(xpath('//list/text()', xmldata::xml))::text AS listname 
FROM item WHERE item.shop_id = 2;


----------------------------------------SIM_PRODUCTS----------------------------------------------
-- ÄNDERN ALS RELATION ZWISCHEN ZWEI ITEM IDS
DROP TABLE IF EXISTS sim_products;
CREATE TABLE sim_products(
	sim_product_id SERIAL PRIMARY KEY,
	item_id integer,
	title text,
	asin text,
FOREIGN KEY (item_id) REFERENCES item(item_id))
;
-- similar products aus leipzig einfügen
INSERT INTO sim_products (item_id, asin, title)	
SELECT item_id, unnest(xpath('//sim_product/asin/text()', xmldata::xml))::text AS asin, unnest(xpath('//sim_product/title/text()', xmldata::xml))::text AS title
FROM item WHERE item.shop_id = 1;	
--similar products aus dresden einfügen
INSERT INTO sim_products (item_id, asin,title)
SELECT item_id, unnest(xpath('/similars/item/@asin', xmldata::xml))::text AS asin, unnest(xpath('/similars/item/text()', xmldata::xml))::text AS title
FROM item WHERE item.shop_id = 2;
-- neue spalte für zweite item_id erstellen
ALTER TABLE sim_products ADD COLUMN item_id2 INTEGER;
-- die neue spalte füllen mit den ids, die zu den asins in der asin spalte gehören
UPDATE sim_products
SET item_id2 = (
    SELECT item_id
    FROM item
    WHERE item.asin = sim_products.asin
);

---------------------------------AUTHOR--------------------------------------------------------
DROP TABLE IF EXISTS author;
CREATE TABLE author(
author_id SERIAL PRIMARY KEY,
author_name text,
item_id integer,
FOREIGN KEY (item_id) REFERENCES item(item_id)
);
--INSERT AUTHORS FROM LEIPZIG
INSERT INTO author (item_id, author_name)
SELECT item_id, 
 unnest(xpath('//author/@name', xmldata::xml))::text AS author_name
FROM item 
WHERE item.shop_id = 1;
--INSERT AUTHORS FROM DRESDEN
INSERT INTO author (item_id, author_name)
SELECT item_id, 
 unnest(xpath('//author/text()', xmldata::xml))::text AS author_name
FROM item 
WHERE item.shop_id = 2;
---------------------------------ACTOR--------------------------------------------------------
DROP TABLE IF EXISTS actor;
CREATE TABLE actor(
actor_id SERIAL PRIMARY KEY,
actor_name text,
item_id integer,
FOREIGN KEY (item_id) REFERENCES item(item_id)
);
--INSERT ACTORS FROM LEIPZIG
INSERT INTO actor (item_id, actor_name)
SELECT item_id, 
 unnest(xpath('//actor/@name', xmldata::xml))::text AS actor_name
FROM item 
WHERE item.shop_id = 1;
--INSERT ACTORS FROM DRESDEN
INSERT INTO actor (item_id, actor_name)
SELECT item_id, 
 unnest(xpath('//actor/text()', xmldata::xml))::text AS actor_name
FROM item 
WHERE item.shop_id = 2;
---------------------------------ARTIST--------------------------------------------------------
DROP TABLE IF EXISTS artist;
CREATE TABLE artist(
artist_id SERIAL PRIMARY KEY,
artist_name text,
item_id integer,
FOREIGN KEY (item_id) REFERENCES item(item_id)
);
--INSERT ARTISTS FROM LEIPZIG
INSERT INTO artist (item_id, artist_name)
SELECT item_id, 
 unnest(xpath('//artist/@name', xmldata::xml))::text AS artist_name
FROM item 
WHERE item.shop_id = 1;
--INSERT ARTISTS FROM DRESDEN
INSERT INTO artist (item_id, artist_name)
SELECT item_id, 
 unnest(xpath('//artist/text()', xmldata::xml))::text AS artist_name
FROM item 
WHERE item.shop_id = 2;
---------------------------------DIRECTOR--------------------------------------------------------
DROP TABLE IF EXISTS director;
CREATE TABLE director(
director_id SERIAL PRIMARY KEY,
director_name text,
item_id integer,
FOREIGN KEY (item_id) REFERENCES item(item_id)
);
--INSERT DIRECTORS FROM LEIPZIG
INSERT INTO director (item_id, director_name)
SELECT item_id, 
 unnest(xpath('//director/@name', xmldata::xml))::text AS director_name
FROM item 
WHERE item.shop_id = 1;
--INSERT DIRECTORS FROM DRESDEN
INSERT INTO director (item_id, director_name)
SELECT item_id, 
 unnest(xpath('//director/text()', xmldata::xml))::text AS director_name
FROM item 
WHERE item.shop_id = 2;
---------------------------------CREATOR--------------------------------------------------------
DROP TABLE IF EXISTS creator;
CREATE TABLE creator(
creator_id SERIAL PRIMARY KEY,
creator_name text,
item_id integer,
FOREIGN KEY (item_id) REFERENCES item(item_id)
);
--INSERT CREATORS FROM LEIPZIG
INSERT INTO creator (item_id, creator_name)
SELECT item_id, 
 unnest(xpath('//creator/@name', xmldata::xml))::text AS creator_name
FROM item 
WHERE item.shop_id = 1;
--INSERT CREATORS FROM DRESDEN
INSERT INTO creator (item_id, creator_name)
SELECT item_id, 
 unnest(xpath('//creator/text()', xmldata::xml))::text AS creator_name
FROM item 
WHERE item.shop_id = 2;
------------------------------------LABEL----------------------------------------------------------
--key von cdspec nehmen
DROP TABLE IF EXISTS labels;
CREATE TABLE labels(
label_id SERIAL PRIMARY KEY,
label_name text,
item_id integer,
FOREIGN KEY (item_id) REFERENCES item(item_id)
);
--INSERT LABELS FROM LEIPZIG
INSERT INTO labels (item_id, label_name)
SELECT item_id, 
 unnest(xpath('//label/@name', xmldata::xml))::text AS label_name
FROM item 
WHERE item.shop_id = 1;
--INSERT LABELS FROM DRESDEN
INSERT INTO labels (item_id, label_name)
SELECT item_id, 
 unnest(xpath('//label/text()', xmldata::xml))::text AS label_name
FROM item 
WHERE item.shop_id = 2;

------------------------------------PUBLISHERS----------------------------------------------------------
--key von bookspec nehmen
DROP TABLE IF EXISTS publishers;
CREATE TABLE publishers(
publisher_id SERIAL PRIMARY KEY,
publisher_name text,
item_id integer,
FOREIGN KEY (item_id) REFERENCES item(item_id)
);
--INSERT PUBLISHERS FROM LEIPZIG
INSERT INTO publishers (item_id, publisher_name)
SELECT item_id, 
 unnest(xpath('//publisher/@name', xmldata::xml))::text AS publisher_name
FROM item 
WHERE item.shop_id = 1;
--INSERT PUBLISHERS FROM DRESDEN
INSERT INTO publishers (item_id, publisher_name)
SELECT item_id, 
 unnest(xpath('//publisher/text()', xmldata::xml))::text AS publisher_name
FROM item 
WHERE item.shop_id = 2;

------------------------------------STUDIOS----------------------------------------------------------
--key von dvdspec nehmen: ALS FOREIGN KEY MUSS MAN NOCH DEN KEY VON DVDSPEC NEHMEN DAMIT NUR DVDs EIN STUDIO HABEN KÖNNEN, andere analog
DROP TABLE IF EXISTS studios;
CREATE TABLE studios(
studio_id SERIAL PRIMARY KEY,
studio_name text,
item_id integer,
FOREIGN KEY (item_id) REFERENCES item(item_id)
);
--INSERT STUDIOS FROM LEIPZIG
INSERT INTO studios (item_id, studio_name)
SELECT item_id, 
 unnest(xpath('//studio/@name', xmldata::xml))::text AS studio_name
FROM item 
WHERE item.shop_id = 1;
--INSERT STUDIOS FROM DRESDEN
INSERT INTO studios (item_id, studio_name)
SELECT item_id, 
 unnest(xpath('//studio/text()', xmldata::xml))::text AS studio_name
FROM item 
WHERE item.shop_id = 2;


--------------------------------------TRACKS-----------------------------------------
DROP TABLE IF EXISTS tracks;
CREATE TABLE tracks (
  track_id SERIAL PRIMARY KEY,
  item_id integer,
  track_title text,
  FOREIGN KEY (item_id) REFERENCES item(item_id)
);
INSERT INTO tracks (item_id, track_title)
SELECT item_id,
unnest(xpath('//tracks/title/text()', xmldata::xml))::text AS track_title
FROM item;

-------------------------------------------AUDIOTEXT------------------------------------
DROP TABLE IF EXISTS audiotext;
CREATE TABLE audiotext (
    item_id INTEGER REFERENCES item(item_id),  
    type VARCHAR,
    language VARCHAR,
    audioformat VARCHAR
);
--DATEN EINFÜGEN
WITH xml_data AS (
    SELECT 
        item_id,
        xpath('//audiotext/language', xmldata) AS language_elements,
        xpath('//audiotext/audioformat', xmldata) AS audioformat_elements
    FROM 
        item
),
parsed_data AS (
    SELECT
        item_id,
        UNNEST(language_elements) AS language_element,
        UNNEST(audioformat_elements) AS audioformat_element
    FROM 
        xml_data
),
extracted_data AS (
    SELECT
        item_id,
	    unnest(xpath('//text()', language_element::xml))::text AS language_text,
	unnest(xpath('//@type', language_element::xml))::text AS language_type,
	unnest(xpath('//text()', audioformat_element::xml))::text AS audioformat_text
    FROM 
        parsed_data
)
INSERT INTO audiotext (item_id, type, language, audioformat)
SELECT
    item_id,
    language_type,
    language_text,
    audioformat_text
FROM 
    extracted_data;
	

---------------------------------------------BOOKSPEC---------------------------------------------------------------
DROP TABLE IF EXISTS bookspec;
CREATE TABLE bookspec(
  book_id SERIAL PRIMARY KEY,
  item_id integer,
  binding text,
  edition text,
  isbn text,
  package_weight text,
  package_height text,
  package_length text,
  pages text,
  publication_date text,
  FOREIGN KEY (item_id) REFERENCES item(item_id)
);

INSERT INTO bookspec (item_id, binding, edition, isbn, package_weight, package_height, package_length, pages, publication_date)
SELECT item_id,
unnest(xpath('//bookspec/binding/text()', xmldata::xml))::text AS binding,
unnest(xpath('//bookspec/edition/@val', xmldata::xml))::text AS edition,
unnest(xpath('//bookspec/isbn/@val', xmldata::xml))::text AS isbn,
unnest(xpath('//bookspec/package/@weight', xmldata::xml))::text AS package_weight,
unnest(xpath('//bookspec/package/@height', xmldata::xml))::text AS package_height,
unnest(xpath('//bookspec/package/@length', xmldata::xml))::text AS package_length,
unnest(xpath('//bookspec/pages/text()', xmldata::xml))::text AS pages,
unnest(xpath('//bookspec/publication/@date', xmldata::xml))::text AS publication_date
FROM item 
WHERE item.pgroup = 'Book';


--------------------------------------DVDSPEC--------------------------------------------------------------------
DROP TABLE IF EXISTS dvdspec;
CREATE TABLE dvdspec(
  dvd_id SERIAL PRIMARY KEY,
  item_id integer,
  aspectratio text,
  format text,
  regioncode text,
  releasedate text,
  runningtime text,
  theatr_release text,
  upc text,
  FOREIGN KEY (item_id) REFERENCES item(item_id)
);

INSERT INTO dvdspec (item_id, aspectratio, format, regioncode, releasedate, runningtime, theatr_release, upc)
SELECT item_id,
unnest(xpath('//dvdspec/aspectratio/text()', xmldata::xml))::text AS aspectratio,
unnest(xpath('//dvdspec/format/text()', xmldata::xml))::text AS format,
unnest(xpath('//dvdspec/regioncode/text()', xmldata::xml))::text AS regioncode,
unnest(xpath('//dvdspec/releasedate/text()', xmldata::xml))::text AS releasedate,
unnest(xpath('//dvdspec/runningtime/text()', xmldata::xml))::text AS runningtime,
unnest(xpath('//dvdspec/theatr_release/text()', xmldata::xml))::text AS theatr_release,
unnest(xpath('//dvdspec/upc/@val', xmldata::xml))::text AS upc
FROM item
WHERE pgroup = 'DVD';


-----------------------------------------MUSICSPEC------------------------------------------------------
DROP TABLE IF EXISTS musicspec;
CREATE TABLE musicspec(
  cd_id SERIAL PRIMARY KEY,
  item_id integer,
  binding text,
  format text,
  num_discs text,
  releasedate text,
  upc text,
  FOREIGN KEY (item_id) REFERENCES item(item_id)
);

INSERT INTO musicspec (item_id, binding, format, num_discs, releasedate, upc)
SELECT item_id,
unnest(xpath('//musicspec/binding/text()', xmldata::xml))::text AS binding,
unnest(xpath('//musicspec/format/@value', xmldata::xml))::text AS format,
unnest(xpath('//musicspec/num_discs/text()', xmldata::xml))::text AS num_discs,
unnest(xpath('//musicspec/releasedate/text()', xmldata::xml))::text AS releasedate,
unnest(xpath('//musicspec/upc/text()', xmldata::xml))::text AS upc
FROM item WHERE pgroup = 'Music' AND shop_id = 1;


INSERT INTO musicspec (item_id, binding, format, num_discs, releasedate, upc)
SELECT item_id,
unnest(xpath('//musicspec/binding/text()', xmldata::xml))::text AS binding,
unnest(xpath('//musicspec/format/text()', xmldata::xml))::text AS format,
unnest(xpath('//musicspec/num_discs/text()', xmldata::xml))::text AS num_discs,
unnest(xpath('//musicspec/releasedate/text()', xmldata::xml))::text AS releasedate,
unnest(xpath('//musicspec/upc/text()', xmldata::xml))::text AS upc
FROM item WHERE pgroup = 'Music' AND shop_id = 2;

