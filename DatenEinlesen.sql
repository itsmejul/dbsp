---------------------------------------------------------------------------------------
----read data from dresden into xmldatadresden table, split by items
DROP TABLE IF EXISTS xmldatadresden CASCADE;
CREATE TABLE xmldatadresden (
 item xml);
INSERT INTO xmldatadresden (item)
SELECT unnest(xpath('//item[not(parent::similars)]', XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:\temp\dresden.xml'), 'UTF8')::xml)));
--SELECT unnest(xpath('//item[not(parent::similars)]', XMLPARSE(DOCUMENT regexp_replace(convert_from(pg_read_binary_file('C:\\temp\\dresden.xml'), 'LATIN1')::text, 'encoding="latin1"', 'encoding="UTF-8"', 'g')::text)));
		
		
----READ DATA FROM LEIPZIG INTO TEMPORARY TABLE,SPLIT BY ITEMS
DROP TABLE IF EXISTS xmldataleipzig CASCADE;
CREATE TABLE xmldataleipzig (
 item xml);
INSERT INTO xmldataleipzig (item)
SELECT unnest(xpath('//item', XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:\temp\leipzig_transformed.xml'), 'UTF8')::xml)));
	
-----------------------------------SHOPS-----------------------------------------------
-- Create a temporary table for XML data
DROP TABLE IF EXISTS temp_shops CASCADE;
CREATE TEMP TABLE temp_shops (
    xmldata XML
);

-- Insert XML data into the temporary table
INSERT INTO temp_shops (xmldata)
SELECT unnest(xpath('//shop', XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:/temp/leipzig_transformed.xml'), 'UTF8')::xml)));

INSERT INTO temp_shops (xmldata)
SELECT unnest(xpath('//shop', XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:/temp/dresden.xml'), 'UTF8')::xml)));
--SELECT unnest(xpath('//item[not(parent::similars)]', XMLPARSE(DOCUMENT regexp_replace(convert_from(pg_read_binary_file('C:\\temp\\dresden.xml'), 'LATIN1')::text, 'encoding="latin1"', 'encoding="UTF-8"', 'g')::text)));

-- Insert extracted values into the main shops table, ensuring no NULL values are inserted
INSERT INTO shops (shop_name, shop_street, shop_zip)
SELECT 
    (xpath('string(//shop/@name)', xmldata))[1]::text AS shop_name,
    (xpath('string(//shop/@street)', xmldata))[1]::text AS shop_street,
    (xpath('string(//shop/@zip)', xmldata))[1]::text::INTEGER AS shop_zip
FROM temp_shops
WHERE (xpath('string(//shop/@name)', xmldata))[1] IS NOT NULL
  AND (xpath('string(//shop/@street)', xmldata))[1] IS NOT NULL
  AND (xpath('string(//shop/@zip)', xmldata))[1] IS NOT NULL;

-- Drop the temporary table
DROP TABLE temp_shops;



------------------------------------ITEM-------------------------------------------------------------
-- ERSTELLE TABELLE ITEM
DROP TABLE IF EXISTS temp_item CASCADE;
CREATE TABLE temp_item (
    item_id SERIAL PRIMARY KEY, --eventuell Leipzig und Dresden gleiche asin für verschiedene Produkte 
    title VARCHAR(255) NOT NULL,
    pgroup VARCHAR(5),
    shop_id INTEGER,
    salesrank INTEGER,
    asin VARCHAR(10) NOT NULL,
	CONSTRAINT asin_length CHECK (LENGTH(asin) = 10),
    ean VARCHAR(13) NOT NULL,
	CONSTRAINT ean_numeric CHECK (ean ~ '^[0-9]{13}$'),
    picture TEXT,
    detailpage TEXT,
    xmldata XML
);
--SELECT * FROM item WHERE title is null
--DATEN AUS LEIPZIG IN ITEM EINFÜGEN
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT 
            item,
            (xpath('/item/title/text()', item))[1]::text AS title,
            (xpath('/item/@pgroup', item))[1]::text AS pgroup,
            COALESCE(NULLIF((xpath('/item/@salesrank', item))[1]::text, '')::INTEGER, 0) AS salesrank,
            (xpath('/item/@asin', item))[1]::text AS asin,
            (xpath('/item/@ean', item))[1]::text AS ean,
            (xpath('/item/@picture', item))[1]::text AS picture,
            (xpath('/item/@detailpage', item))[1]::text AS detailpage
        FROM xmldataleipzig
    LOOP
        BEGIN
            INSERT INTO temp_item (title, pgroup, shop_id, salesrank, asin, ean, picture, detailpage, xmldata)
            VALUES (rec.title, rec.pgroup, 1, rec.salesrank, rec.asin, rec.ean, rec.picture, rec.detailpage, rec.item);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('Leipzig Item', rec.asin, SQLERRM, 0, rec.item);
        END;
    END LOOP;
END $$;

-- DATEN AUS DRESDEN IN ITEM EINFÜGEN
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT 
            item,
            (xpath('/item/title/text()', item))[1]::text AS title,
            (xpath('/item/@pgroup', item))[1]::text AS pgroup,
            COALESCE(NULLIF((xpath('/item/@salesrank', item))[1]::text, '')::INTEGER, 0) AS salesrank,
            (xpath('/item/@asin', item))[1]::text AS asin,
            (xpath('/item/ean/text()', item))[1]::text AS ean,
            (xpath('/item/details/@img', item))[1]::text AS picture,
            (xpath('/item/details/text()', item))[1]::text AS detailpage
        FROM xmldatadresden
    LOOP
        BEGIN
            INSERT INTO temp_item (title, pgroup, shop_id, salesrank, asin, ean, picture, detailpage, xmldata)
            VALUES (rec.title, rec.pgroup, 2, rec.salesrank, rec.asin, rec.ean, rec.picture, rec.detailpage, rec.item);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('Dresden Item', rec.asin, SQLERRM, 0, rec.item);
        END;
    END LOOP;
END $$;




--------------------------------------PRICE---------------------------------------------------------------------
-- PRICE ALS EXTRA RELATION, DA EIN ITEM MEHRERE PREISE HABEN KANN (zB NEU UND SECOND HAND)
DROP TABLE IF EXISTS temp_price CASCADE;
CREATE TABLE temp_price(
    price_id SERIAL PRIMARY KEY,
    item_id INTEGER,
	asin VARCHAR(10),
    price_state VARCHAR(20),
    price_mult DECIMAL(10,2), -- DECIMAL(2,2) ist zu klein, DECIMAL(10,2) ist üblicher
    price_currency VARCHAR(3),
    price_value INTEGER,
    price_shop_id INTEGER,
    FOREIGN KEY (item_id) REFERENCES temp_item(item_id)
);
--SELECT * FROM price
-- PREISE AUS ITEMS IN DIE PRICE-TABELLE EINFÜGEN
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT 
            item_id,
			asin,
            unnest(xpath('//price/@state', xmldata::xml))::text AS price_state,
            unnest(xpath('//price/@mult', xmldata::xml))::text::DECIMAL AS price_mult,
            unnest(xpath('//price/@currency', xmldata::xml))::text AS price_currency,
            unnest(xpath('//price/text()', xmldata::xml))::text::INTEGER AS price_value,
            shop_id,
            xmldata
        FROM temp_item
    LOOP
        BEGIN
            INSERT INTO temp_price (item_id, asin, price_state, price_mult, price_currency, price_value, price_shop_id)
            VALUES (rec.item_id, rec.asin, rec.price_state, rec.price_mult, rec.price_currency, rec.price_value, rec.shop_id);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('Price', 'Multiple', SQLERRM, rec.item_id, rec.xmldata);
        END;
    END LOOP;
END $$;

--SELECT * FROM error_input WHERE entity_name = 'Price'
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
        temp_item
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
    temp_price p
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
    temp_item 
USING 
    duplicate_items di
WHERE 
    temp_item.item_id = di.duplicate_item_id
AND 
    temp_item.item_id <> di.item_id_to_keep;
-----------------------------------
--delete restliche duplikate
DELETE FROM 
    temp_item t1
USING 
    temp_item t2
WHERE 
    t1.asin = t2.asin AND t1.item_id > t2.item_id;
--SELECT * FROM temp_item
-----------------------------------------------------------------------------------------
--SELECT * FROM item WHERE asin = 'B00065VRX8'
--SELECT * FROM error_input WHERE entity_name = 'Item to Item'
--temp_item --> item

DO
$$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN SELECT asin, title, pgroup, salesrank, ean, picture, detailpage FROM temp_item
    LOOP
        BEGIN
            INSERT INTO item (asin, title, pgroup, salesrank, ean, picture, detailpage)
            VALUES (rec.asin, rec.title, rec.pgroup, rec.salesrank, rec.ean, rec.picture, rec.detailpage);
        EXCEPTION
            WHEN OTHERS THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('Item to Item', rec.asin, SQLERRM, null, NULL);
        END;
    END LOOP;
END;
$$;

--temp_price --> price
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT
			price_id,
		    asin,
		    price_state,
		    price_mult,
		    price_currency,
		    price_value,
		    price_shop_id
        FROM temp_price
    LOOP
        BEGIN
            INSERT INTO price (asin, price_state, price_mult, price_currency, price_value, price_shop_id)
            VALUES (rec.asin, rec.price_state, rec.price_mult, rec.price_currency, rec.price_value, rec.price_shop_id);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('Price to Price', rec.asin, SQLERRM, rec.price_id, null);
        END;
    END LOOP;
END $$;

--SELECT * FROM price
--SELECT * FROM error_input WHERE entity_name = 'Price to Price'
-------------------------------------LISTS-----------------------------------------------
--SELECT * FROM lists
--FÜGE LISTMANIA LISTEN VON LEIPZIG EIN
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT 
			asin, 
			unnest(xpath('//list/@name', xmldata::xml))::text AS listname 
			FROM temp_item WHERE temp_item.shop_id = 1
    LOOP
        BEGIN
            INSERT INTO lists (asin, listname)
            VALUES (rec.asin, rec.listname);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('Lists', rec.asin, SQLERRM, 1, null);
        END;
    END LOOP;
END $$;	
--FÜGE LISTMANIA LISTEN VON DRESDEN EIN
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT asin, 
			unnest(xpath('//list/text()', xmldata::xml))::text AS listname 
			FROM temp_item WHERE temp_item.shop_id = 2
    LOOP
        BEGIN
            INSERT INTO lists (asin, listname)
            VALUES (rec.asin, rec.listname);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('Lists', rec.asin, SQLERRM, 2, null);
        END;
    END LOOP;
END $$;	

----------------------------------------SIM_PRODUCTS----------------------------------------------
--SELECT * FROM sim_products
-- similar products aus leipzig einfügen
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT 
			asin, 
			unnest(xpath('//sim_product/asin/text()', xmldata::xml))::text AS asin_similar
		--	unnest(xpath('//sim_product/title/text()', xmldata::xml))::text AS title
			FROM temp_item WHERE temp_item.shop_id = 1
    LOOP
        BEGIN
            INSERT INTO sim_products (asin_original, asin_similar)
            VALUES (rec.asin, rec.asin_similar);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('SIM_ITEM', rec.asin, SQLERRM, 1, null);
        END;
    END LOOP;
END $$;	
--similar products aus dresden einfügen
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT 
			asin, 
			unnest(xpath('/similars/item/@asin', xmldata::xml))::text AS asin_similar
		 --	unnest(xpath('/similars/item/text()', xmldata::xml))::text AS title
			FROM temp_item WHERE temp_item.shop_id = 2
    LOOP
        BEGIN
            INSERT INTO sim_products (asin_original, asin_similar)
            VALUES (rec.asin, rec.asin_similar);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('SIM_ITEM', rec.asin, SQLERRM, 2, null);
        END;
    END LOOP;
END $$;

---------------------------------AUTHOR--------------------------------------------------------
--SELECT * FROM author
--INSERT AUTHORS FROM LEIPZIG
INSERT INTO author (asin, author_name)
SELECT asin, 
	unnest(xpath('//author/@name', xmldata::xml))::text AS author_name
FROM temp_item 
WHERE temp_item.shop_id = 1;
--INSERT AUTHORS FROM DRESDEN
INSERT INTO author (asin, author_name)
SELECT asin, 
	unnest(xpath('//author/text()', xmldata::xml))::text AS author_name
FROM temp_item 
WHERE temp_item.shop_id = 2;

---------------------------------ACTOR--------------------------------------------------------
--SELECT * FROM actor
--INSERT ACTORS FROM LEIPZIG
INSERT INTO actor (asin, actor_name)
SELECT asin, 
	unnest(xpath('//actor/@name', xmldata::xml))::text AS actor_name
FROM temp_item 
WHERE temp_item.shop_id = 1;
--INSERT ACTORS FROM DRESDEN
INSERT INTO actor (asin, actor_name)
SELECT asin, 
	unnest(xpath('//actor/text()', xmldata::xml))::text AS actor_name
FROM temp_item 
WHERE temp_item.shop_id = 2;

---------------------------------ARTIST--------------------------------------------------------
--SELECT * FROM artist
--INSERT ARTISTS FROM LEIPZIG
INSERT INTO artist (asin, artist_name)
SELECT asin, 
	unnest(xpath('//artist/@name', xmldata::xml))::text AS artist_name
FROM temp_item 
WHERE temp_item.shop_id = 1;
--INSERT ARTISTS FROM DRESDEN
INSERT INTO artist (asin, artist_name)
SELECT asin, 
	unnest(xpath('//artist/text()', xmldata::xml))::text AS artist_name
FROM temp_item 
WHERE temp_item.shop_id = 2;

---------------------------------DIRECTOR--------------------------------------------------------
--SELECT * FROM director
--INSERT DIRECTORS FROM LEIPZIG
INSERT INTO director (asin, director_name)
SELECT asin, 
	unnest(xpath('//director/@name', xmldata::xml))::text AS director_name
FROM temp_item 
WHERE temp_item.shop_id = 1;
--INSERT DIRECTORS FROM DRESDEN
INSERT INTO director (asin, director_name)
SELECT asin, 
	unnest(xpath('//director/text()', xmldata::xml))::text AS director_name
FROM temp_item 
WHERE temp_item.shop_id = 2;

---------------------------------CREATOR--------------------------------------------------------
--SELECT * FROM creator
--INSERT CREATORS FROM LEIPZIG
INSERT INTO creator (asin, creator_name)
SELECT asin, 
	unnest(xpath('//creator/@name', xmldata::xml))::text AS creator_name
FROM temp_item 
WHERE temp_item.shop_id = 1;
--INSERT CREATORS FROM DRESDEN
INSERT INTO creator (asin, creator_name)
SELECT asin, 
	unnest(xpath('//creator/text()', xmldata::xml))::text AS creator_name
FROM temp_item 
WHERE temp_item.shop_id = 2;

------------------------------------LABEL-------------------------------------------------------
--SELECT * FROM labels
--INSERT LABELS FROM LEIPZIG
INSERT INTO labels (asin, label_name)
SELECT asin, 
	unnest(xpath('//label/@name', xmldata::xml))::text AS label_name
FROM temp_item 
WHERE temp_item.shop_id = 1;
--INSERT LABELS FROM DRESDEN
INSERT INTO labels (asin, label_name)
SELECT asin, 
	unnest(xpath('//label/text()', xmldata::xml))::text AS label_name
FROM temp_item 
WHERE temp_item.shop_id = 2;

------------------------------------PUBLISHERS---------------------------------------------------
--SELECT * FROM publishers
--INSERT PUBLISHERS FROM LEIPZIG
INSERT INTO publishers (asin, publisher_name)
SELECT asin, 
	unnest(xpath('//publisher/@name', xmldata::xml))::text AS publisher_name
FROM temp_item 
WHERE temp_item.shop_id = 1;
--INSERT PUBLISHERS FROM DRESDEN
INSERT INTO publishers (asin, publisher_name)
SELECT asin, 
	unnest(xpath('//publisher/text()', xmldata::xml))::text AS publisher_name
FROM temp_item 
WHERE temp_item.shop_id = 2;

------------------------------------STUDIOS------------------------------------------------------
--SELECT * FROM studios
--INSERT STUDIOS FROM LEIPZIG
INSERT INTO studios (asin, studio_name)
SELECT asin, 
	unnest(xpath('//studio/@name', xmldata::xml))::text AS studio_name
FROM temp_item 
WHERE temp_item.shop_id = 1;
--INSERT STUDIOS FROM DRESDEN
INSERT INTO studios (asin, studio_name)
SELECT asin, 
	unnest(xpath('//studio/text()', xmldata::xml))::text AS studio_name
FROM temp_item 
WHERE temp_item.shop_id = 2;

--------------------------------------TRACKS----------------------------------------------------
--SELECT * FROM tracks
INSERT INTO tracks (asin, track_title)
SELECT asin,
unnest(xpath('//tracks/title/text()', xmldata::xml))::text AS track_title
FROM temp_item;

-------------------------------------------AUDIOTEXT--------------------------------------------
--SELECT * FROM audiotext
--DATEN EINFÜGEN
WITH xml_data AS (
    SELECT 
        asin,
        xpath('//audiotext/language', xmldata) AS language_elements,
        xpath('//audiotext/audioformat', xmldata) AS audioformat_elements
    FROM 
        temp_item
),
parsed_data AS (
    SELECT
        asin,
        UNNEST(language_elements) AS language_element,
        UNNEST(audioformat_elements) AS audioformat_element
    FROM 
        xml_data
),
extracted_data AS (
    SELECT
        asin,
	    unnest(xpath('//text()', language_element::xml))::text AS language_text,
	unnest(xpath('//@type', language_element::xml))::text AS language_type,
	unnest(xpath('//text()', audioformat_element::xml))::text AS audioformat_text
    FROM 
        parsed_data
)
INSERT INTO audiotext (asin, type, language, audioformat)
SELECT
    asin,
    language_type,
    language_text,
    audioformat_text
FROM 
    extracted_data;

---------------------------------------------BOOKSPEC--------------------------------------------
--SELECT * FROM bookspec
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT 
            item_id,
			asin,
            unnest(xpath('//bookspec/binding/text()', xmldata::xml))::text AS binding,
            unnest(xpath('//bookspec/edition/@val', xmldata::xml))::text AS edition,
            unnest(xpath('//bookspec/isbn/@val', xmldata::xml))::text AS isbn,
            (SELECT COALESCE((SELECT unnest(xpath('//bookspec/pages/@weight', xmldata::xml))::text)::integer, 0)) AS package_weight,
			(SELECT COALESCE((SELECT unnest(xpath('//bookspec/pages/@height', xmldata::xml))::text)::integer, 0)) AS package_height,
			(SELECT COALESCE((SELECT unnest(xpath('//bookspec/pages/@length', xmldata::xml))::text)::integer, 0)) AS package_length,
			(SELECT COALESCE((SELECT unnest(xpath('//bookspec/pages/text()', xmldata::xml))::text::integer), 0)) AS pages,
            NULLIF(unnest(xpath('//bookspec/publication/@date', xmldata::xml))::text, '')::DATE AS publication_date, -- Behandlung leerer Datumswerte
            xmldata
        FROM temp_item 
        WHERE temp_item.pgroup = 'Book'
    LOOP
        BEGIN
            INSERT INTO bookspec (asin, binding, edition, isbn, package_weight, package_height, package_length, pages, publication_date)
            VALUES (rec.asin, rec.binding, rec.edition, rec.isbn, rec.package_weight, rec.package_height, rec.package_length, rec.pages, rec.publication_date);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('Bookspec', rec.asin, SQLERRM, rec.item_id, rec.xmldata);
        END;
    END LOOP;
END $$;


--------------------------------------DVDSPEC--------------------------------------------------------------------
--SELECT * FROM dvdspec WHERE aspectratio IS NOT null AND aspectratio !='4:3' AND aspectratio !='16:9'
--SELECT * FROM error_input WHERE entity_name ='DVDspec'
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT 
            item_id,
			asin,
            unnest(xpath('//dvdspec/aspectratio/text()', xmldata::xml))::text AS aspectratio,
            unnest(xpath('//dvdspec/format/text()', xmldata::xml))::text AS format,
			COALESCE((SELECT unnest(xpath('//dvdspec/regioncode/text()', xmldata::xml))::text::integer), 0) AS regioncode,
            NULLIF(unnest(xpath('//dvdspec/releasedate/text()', xmldata::xml))::text, '')::DATE AS releasedate,
            COALESCE((SELECT unnest(xpath('//dvdspec/runningtime/text()', xmldata::xml))::text::integer), 0) AS runningtime,
            unnest(xpath('//dvdspec/theatr_release/text()', xmldata::xml))::text AS theatr_release,
            unnest(xpath('//dvdspec/upc/@val', xmldata::xml))::text AS upc,
            xmldata
        FROM temp_item
        WHERE temp_item.pgroup = 'DVD'
    LOOP
        BEGIN
            INSERT INTO dvdspec (asin, aspectratio, format, regioncode, releasedate, runningtime, theatr_release, upc)
            VALUES (rec.asin, rec.aspectratio, rec.format, rec.regioncode, rec.releasedate, rec.runningtime, rec.theatr_release, rec.upc);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('DVDspec', rec.asin, SQLERRM, rec.item_id, rec.xmldata);
        END;
    END LOOP;
END $$;

-----------------------------------------MUSICSPEC------------------------------------------------------
--SELECT * FROM musicspec
-- MUSIKSPEZIFIKATIONEN AUS ITEMS IN DIE MUSICSPEC-TABELLE EINFÜGEN (SHOP 1)
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT 
            item_id,
			asin,
            unnest(xpath('//musicspec/binding/text()', xmldata::xml))::text AS binding,
            unnest(xpath('//musicspec/format/@value', xmldata::xml))::text AS format,
            COALESCE((SELECT unnest(xpath('//musicspec/num_discs/text()', xmldata::xml))::text::integer), 0) AS num_discs,
            NULLIF(unnest(xpath('//musicspec/releasedate/text()', xmldata::xml))::text, '')::DATE AS releasedate,
            unnest(xpath('//musicspec/upc/text()', xmldata::xml))::text AS upc,
            xmldata
        FROM temp_item 
        WHERE pgroup = 'Music' AND shop_id = 1
    LOOP
        BEGIN
            INSERT INTO musicspec (asin, binding, format, num_discs, releasedate, upc)
            VALUES (rec.asin, rec.binding, rec.format, rec.num_discs, rec.releasedate, rec.upc);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('Musicspec', rec.asin, SQLERRM, rec.item_id, rec.xmldata);
        END;
    END LOOP;
END $$;

-- MUSIKSPEZIFIKATIONEN AUS ITEMS IN DIE MUSICSPEC-TABELLE EINFÜGEN (SHOP 2)
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT 
            item_id,
			asin,
            unnest(xpath('//musicspec/binding/text()', xmldata::xml))::text AS binding,
            unnest(xpath('//musicspec/format/text()', xmldata::xml))::text AS format,
            COALESCE((SELECT unnest(xpath('//musicspec/num_discs/text()', xmldata::xml))::text::integer), 0) AS num_discs,
            NULLIF(unnest(xpath('//musicspec/releasedate/text()', xmldata::xml))::text, '')::DATE AS releasedate,
            unnest(xpath('//musicspec/upc/text()', xmldata::xml))::text AS upc,
            xmldata
        FROM temp_item 
        WHERE pgroup = 'Music' AND shop_id = 2
    LOOP
        BEGIN
            INSERT INTO musicspec (asin, binding, format, num_discs, releasedate, upc)
            VALUES (rec.asin, rec.binding, rec.format, rec.num_discs, rec.releasedate, rec.upc);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('Musicspec', rec.asin, SQLERRM, rec.item_id, rec.xmldata);
        END;
    END LOOP;
END $$;
---------------------------------------------------------------------------------------------------------
--Drop the temporary table
DROP TABLE IF EXISTS temp_price;
DROP TABLE IF EXISTS temp_item;
DROP TABLE IF EXISTS xmldatadresden;
DROP TABLE IF EXISTS xmldataleipzig;

-------------Reviews-------------------------------------------------------------------------------------
-- Create a temporary table for importing the CSV data
CREATE TEMP TABLE temp_product_reviews (
    review_id SERIAL PRIMARY KEY,
    product TEXT, 
    rating TEXT,
    helpful TEXT,
    reviewdate TEXT,
    username TEXT,
    summary TEXT,
    review_content TEXT
);

-- Copy data from the CSV file into the temporary table
COPY temp_product_reviews (product, rating, helpful, reviewdate, username, summary, review_content)
FROM 'C:\\temp\\reviews.csv'
DELIMITER ','
CSV HEADER;

-- Handle data transformation and insertion into the main table
DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN 
        SELECT
            review_id,
            product,
            CASE WHEN rating ~ '^[0-9]+$' THEN CAST(rating AS INTEGER) ELSE NULL END AS rating,
            CASE WHEN helpful ~ '^[0-9]+$' THEN CAST(helpful AS INTEGER) ELSE NULL END AS helpful,
            CASE WHEN reviewdate ~ '^\d{4}-\d{2}-\d{2}$' THEN CAST(reviewdate AS DATE) ELSE NULL END AS reviewdate,
            username,
            summary,
            review_content
        FROM temp_product_reviews
    LOOP
        BEGIN
            INSERT INTO product_reviews (asin, rating, helpful, reviewdate, username, summary, review_content)
            VALUES (rec.product, rec.rating, rec.helpful, rec.reviewdate, rec.username, rec.summary, rec.review_content);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('Review', rec.product, SQLERRM, rec.review_id, null);
        END;
    END LOOP;
END $$;

-- Drop the temporary table
DROP TABLE temp_product_reviews;


--SELECT * FROM product_reviews
--SELECT * FROM error_input WHERE entity_name = 'Review'

--------------------------------categories----------------------------------------------------------------
DROP TABLE IF EXISTS xml_data CASCADE;
CREATE TABLE xml_data (content XML);
INSERT INTO xml_data (content) 
--SELECT unnest(xpath('//category[not(parent::category)]',XMLPARSE(DOCUMENT regexp_replace(convert_from(pg_read_binary_file('C:\temp\categories.xml'), 'LATIN1')::text, 'encoding="latin1"', 'encoding="UTF-8"', 'g')::text)));
SELECT unnest(xpath('//categories',XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:\temp\categories.xml'), 'UTF8')::xml)));;


CREATE OR REPLACE FUNCTION insert_category(
    cat XML, 
    parent_id INTEGER DEFAULT NULL
) RETURNS INTEGER AS $$
DECLARE
    cat_id INTEGER;
    subcat XML;
    item XML;
    cat_name TEXT;
BEGIN
    -- Extract the category name
    SELECT unnest(xpath('/category/text()', cat::xml))::TEXT INTO cat_name;
    
    -- Insert the category
    INSERT INTO categories (name, parent_id)
    VALUES (cat_name, parent_id)
    RETURNING id INTO cat_id;

    -- Insert items directly under the current category
    FOR item IN (SELECT unnest(xpath('category/item', cat)))
    LOOP
		BEGIN
            INSERT INTO item_categories (asin, category_id)
        	VALUES ((xpath('/item/text()', item))[1]::TEXT, cat_id);
        EXCEPTION
            WHEN others THEN
                INSERT INTO error_input (entity_name, attribute_name, error_message, item_id, xmldata)
                VALUES ('CAT_ITEM', (xpath('/item/text()', item))[1]::TEXT, SQLERRM, null, null);
        END;
    END LOOP;

    -- Process subcategories recursively
    FOR subcat IN (SELECT unnest(xpath('category/category', cat)))
    LOOP
        PERFORM insert_category(subcat, cat_id);
    END LOOP;

    RETURN cat_id;
END;
$$ LANGUAGE plpgsql;

DO $$
DECLARE
    cat XML;
BEGIN
    -- Insert root categories
    FOR cat IN (SELECT unnest(xpath('//categories/category', content)) FROM xml_data)
    LOOP
        PERFORM insert_category(cat);
    END LOOP;
END;
$$;

--SELECT * FROM categories;
--SELECT * FROM item_categories WHERE item_id IS NOT null;
--SELECT * FROM item_categories WHERE item_id IS null;
--SELECT cat1.* , cat2.* FROM categories cat1, categories cat2 WHERE cat1.id = cat2.parent_id;


-----------------------------------------------------------------------

DROP TABLE IF EXISTS xml_data;

-----------------------------------------------------------------------



