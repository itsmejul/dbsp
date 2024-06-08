DROP TABLE IF EXISTS xmldataleipzig CASCADE;
CREATE TABLE xmldataleipzig
AS
SELECT x.*
   FROM xmltable('/shop/item' passing XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:\temp\leipzig_transformed.xml'), 'UTF8'))
   columns title text path 'title',
				 pgroup text path '@pgroup',
				 ean text path '@ean',
				 asin text path '@asin',
				 salesrank text path '@salesrank',
		   picture text path '@picture',
				detailpage text path '@detailpage',
				 price text path 'price',
				pricemult text path 'price/@mult',
				pricecurrency text path 'price/@currency',
				pricestate text path 'price/@state',
				
				bookspecbinding text path 'bookspec/binding',
				musicspecbinding text path 'musicspec/binding',
				dvdspecformat text path 'dvdspec/format',
				 
				listmania xml path 'listmania',
				 similars xml path 'similars',
				directors xml path 'directors',
				 authors xml path 'authors',
				 artists xml path 'artists',
				 creators xml path 'creators',
				 actors xml path 'actors'
				) as x;
		
ALTER TABLE xmldataleipzig
ADD item_id SERIAL PRIMARY KEY;






DROP TABLE IF EXISTS xmldatadresden CASCADE;
CREATE TABLE xmldatadresden (
 item xml,
item_id SERIAL PRIMARY KEY);
INSERT INTO xmldatadresden (item)
SELECT unnest(xpath('//item', XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:\temp\dresden.xml'), 'UTF8')::xml)));
		








DROP TABLE IF EXISTS lists;
CREATE TABLE lists(
	list_id SERIAL PRIMARY KEY,
	item_id integer,
	listname text,
FOREIGN KEY (item_id) REFERENCES xmldataleipzig(item_id));
INSERT INTO lists (item_id, listname)	
SELECT item_id, unnest(xpath('//list/@name', listmania::xml))::text AS listname 
FROM xmldataleipzig;

DROP TABLE IF EXISTS sim_products;
CREATE TABLE sim_products(
	sim_product_id SERIAL PRIMARY KEY,
	item_id integer,
	title text,
	asin text,
FOREIGN KEY (item_id) REFERENCES xmldataleipzig(item_id))
;
INSERT INTO sim_products (item_id, title, asin)	
SELECT item_id, unnest(xpath('//sim_product/asin/text()', similars::xml))::text AS asin, unnest(xpath('//sim_product/title/text()', similars::xml))::text AS title
FROM xmldataleipzig;	

DROP TABLE IF EXISTS directors;
CREATE TABLE directors(
	director_id SERIAL PRIMARY KEY,
	item_id integer,
	director_name text,
FOREIGN KEY (item_id) REFERENCES xmldataleipzig(item_id));
INSERT INTO directors (item_id, director_name)	
SELECT item_id, unnest(xpath('//director/@name', directors::xml))::text AS director_name
FROM xmldataleipzig;

DROP TABLE IF EXISTS authors;
CREATE TABLE authors(
	author_id SERIAL PRIMARY KEY,
	item_id integer,
	author_name text,
FOREIGN KEY (item_id) REFERENCES xmldataleipzig(item_id));
INSERT INTO authors (item_id, author_name)	
SELECT item_id, unnest(xpath('//author/@name', authors::xml))::text AS author_name
FROM xmldataleipzig;

DROP TABLE IF EXISTS actors;
CREATE TABLE actors(
	actor_id SERIAL PRIMARY KEY,
	item_id integer,
	actor_name text,
FOREIGN KEY (item_id) REFERENCES xmldataleipzig(item_id));
INSERT INTO actors (item_id, actor_name)	
SELECT item_id, unnest(xpath('//actor/@name', actors::xml))::text AS actor_name
FROM xmldataleipzig;

DROP TABLE IF EXISTS artists;
CREATE TABLE artists(
	artist_id SERIAL PRIMARY KEY,
	item_id integer,
	artist_name text,
FOREIGN KEY (item_id) REFERENCES xmldataleipzig(item_id));
INSERT INTO artists (item_id, artist_name)	
SELECT item_id, unnest(xpath('//artist/@name', artists::xml))::text AS artist_name
FROM xmldataleipzig;

DROP TABLE IF EXISTS creators;
CREATE TABLE creators(
	creator_id SERIAL PRIMARY KEY,
	item_id integer,
	creator_name text,
FOREIGN KEY (item_id) REFERENCES xmldataleipzig(item_id));
INSERT INTO creators (item_id, creator_name)	
SELECT item_id, unnest(xpath('//creator/@name', creators::xml))::text AS creator_name
FROM xmldataleipzig;

DROP TABLE IF EXISTS book;
CREATE TABLE book
AS
SELECT i.bookspecbinding, i.item_id
FROM xmldataleipzig i
WHERE i.pgroup = 'Book';

DROP TABLE IF EXISTS music;
CREATE TABLE music
AS
SELECT i.musicspecbinding, i.item_id
FROM xmldataleipzig i
WHERE i.pgroup = 'Music';

DROP TABLE IF EXISTS dvd;
CREATE TABLE dvd
AS SELECT i.dvdspecformat, i.item_id
FROM xmldataleipzig i
WHERE i.pgroup = 'DVD';

DROP TABLE IF EXISTS item;
CREATE TABLE item
AS SELECT i.title, i.item_id, i.pgroup
FROM xmldataleipzig i;

