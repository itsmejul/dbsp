--error data
DROP TABLE IF EXISTS error_input CASCADE;
CREATE TABLE error_input (
    reject_id SERIAL PRIMARY KEY,
    entity_name VARCHAR(255),
    attribute_name VARCHAR(255),
    error_message TEXT,
	item_id INTEGER,
    xmldata XML
);
--SELECT * FROM error_input
--SHOPS
DROP TABLE IF EXISTS shops CASCADE;
CREATE TABLE shops (
	shop_id SERIAL PRIMARY KEY,
	shop_name VARCHAR(20) NOT NULL,
	shop_street VARCHAR(50) NOT NULL,
	shop_zip INTEGER NOT NULL
);
--SELECT * FROM shops
--items/ Produkte
DROP TABLE IF EXISTS item CASCADE;
CREATE TABLE item (
    asin VARCHAR(10) PRIMARY KEY,
	CONSTRAINT asin_length CHECK (LENGTH(asin) = 10),
    title VARCHAR(255) NOT NULL,
    pgroup VARCHAR(5),
    salesrank INTEGER,
    ean VARCHAR(13) UNIQUE NOT NULL,--ob NULL akzeptieren mit  OR NULL?
	CONSTRAINT ean_numeric CHECK (ean ~ '^[0-9]{13}$'), --ean evtl auch andere Länge?
    picture TEXT,
    detailpage TEXT
);
--SELECT * FROM item
-- PRICE ALS EXTRA RELATION, DA EIN ITEM MEHRERE PREISE HABEN KANN (zB NEU UND SECOND HAND)
DROP TABLE IF EXISTS price CASCADE;
CREATE TABLE price(
    price_id SERIAL PRIMARY KEY,
    asin VARCHAR(10) NOT NULL,
    price_state VARCHAR(20),
    price_mult DECIMAL(10,2),
    price_currency VARCHAR(3),
    price_value INTEGER,
    price_shop_id INTEGER,
    FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE,
	FOREIGN KEY (price_shop_id) REFERENCES shops(shop_id) ON DELETE CASCADE
);
--SELECT * FROM price
---------------------------------------------------------------------
--ERSTELLE TABELLE FÜR LISTEN
DROP TABLE IF EXISTS lists CASCADE;
CREATE TABLE lists(
	asin VARCHAR(10)NOT NULL,
	listname VARCHAR(255) NOT NULL,
	PRIMARY KEY (asin, listname),
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM lists
--gleiche Items
DROP TABLE IF EXISTS sim_products CASCADE;
CREATE TABLE sim_products(
	sim_product_id SERIAL PRIMARY KEY,
	title VARCHAR(255),
	asin_original VARCHAR(10) NOT NULL,
	asin_similar VARCHAR(10) NOT NULL,
	FOREIGN KEY (asin_original) REFERENCES item(asin) ON DELETE CASCADE,
	FOREIGN KEY (asin_similar) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM sim_products
--Authoren
DROP TABLE IF EXISTS author CASCADE;
CREATE TABLE author(
	author_name VARCHAR(255) NOT NULL,
	asin VARCHAR(10) NOT NULL,
	PRIMARY KEY (author_name, asin),
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM author
--Actors
DROP TABLE IF EXISTS actor CASCADE;
CREATE TABLE actor(
	actor_name VARCHAR(255) NOT NULL,
	asin VARCHAR(10) NOT NULL,
	PRIMARY KEY (actor_name, asin),
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM actor
--Artists
DROP TABLE IF EXISTS artist CASCADE;
CREATE TABLE artist(
	artist_name VARCHAR(255) NOT NULL,
	asin VARCHAR(10) NOT NULL,
	PRIMARY KEY (artist_name, asin),
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM artist
--director
DROP TABLE IF EXISTS director CASCADE;
CREATE TABLE director(
	director_name VARCHAR(255) NOT NULL,
	asin VARCHAR(10) NOT NULL,
	PRIMARY KEY (director_name, asin),
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM director
--creator
DROP TABLE IF EXISTS creator CASCADE;
CREATE TABLE creator(
	creator_name VARCHAR(255) NOT NULL,
	asin VARCHAR(10) NOT NULL,
	PRIMARY KEY (creator_name, asin),
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM creator
--labels
DROP TABLE IF EXISTS labels CASCADE;
CREATE TABLE labels(
	label_name VARCHAR(255) NOT NULL,
	asin VARCHAR(10) NOT NULL,
	PRIMARY KEY (label_name, asin),
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM labels
--publishers
DROP TABLE IF EXISTS publishers CASCADE;
CREATE TABLE publishers(
	publisher_name VARCHAR(255) NOT NULL,
	asin VARCHAR(10) NOT NULL,
	PRIMARY KEY (publisher_name, asin),
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM publishers
--studios
DROP TABLE IF EXISTS studios CASCADE;
CREATE TABLE studios(
	studio_name VARCHAR(255) NOT NULL,
	asin VARCHAR(10) NOT NULL,
	PRIMARY KEY (studio_name, asin),
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM studios
--tracks
DROP TABLE IF EXISTS tracks CASCADE;
CREATE TABLE tracks (
	asin VARCHAR(10) NOT NULL,
	track_title VARCHAR(255) NOT NULL,
	PRIMARY KEY (track_title, asin),
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM tracks
--audiotext
DROP TABLE IF EXISTS audiotext CASCADE;
CREATE TABLE audiotext (
    asin VARCHAR(10) REFERENCES item(asin) ON DELETE CASCADE,  
    type VARCHAR(50) NOT NULL,
    language VARCHAR(20) NOT NULL,
    audioformat VARCHAR(50)
);
--SELECT * FROM audiotext
---------------------------------------------------------------------
--bookspecs
DROP TABLE IF EXISTS bookspec CASCADE;
CREATE TABLE bookspec(
	book_id SERIAL PRIMARY KEY,
	asin VARCHAR(10) NOT NULL,
	binding VARCHAR(50) NOT NULL,
	edition VARCHAR(50),
	isbn VARCHAR(13) UNIQUE NOT NULL,
	CONSTRAINT isbn_numeric CHECK (
    isbn ~ '^[0-9]{9}[0-9X]$' OR isbn ~ '^[0-9]{12}[0-9X]$'), --isbn kann als letztes Zeichen ein X haben
	package_weight INTEGER,
	package_height INTEGER,
	package_length INTEGER,
	pages INTEGER,
	CONSTRAINT valid_pages CHECK (pages > 0),
	publication_date DATE NOT NULL,
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM bookspec
--dvdspecs
DROP TABLE IF EXISTS dvdspec CASCADE;
CREATE TABLE dvdspec(
	dvd_id SERIAL PRIMARY KEY,
	asin VARCHAR(10) NOT NULL,
	aspectratio VARCHAR(10),
	CONSTRAINT aspectratio_format CHECK (aspectratio ~ '^\d+(.\d+)?:\d+$'),
	format VARCHAR(255),
	regioncode INTEGER DEFAULT 0,
	releasedate DATE NOT NULL,
	runningtime INTEGER,
	CONSTRAINT valid_runningtime CHECK (runningtime > 0),
	theatr_release VARCHAR(4),
	CONSTRAINT theatr_release_year CHECK (theatr_release ~ '^[0-9]{4}'),
	upc VARCHAR(12) UNIQUE NOT NULL,
	CONSTRAINT dvd_upc_numeric CHECK (upc ~ '^[0-9]{12}$'), --ob NULL akzeptieren mit  OR NULL? es gibt ja schon ean
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM dvdspec
--musicspecs
DROP TABLE IF EXISTS musicspec CASCADE;
CREATE TABLE musicspec(
	cd_id SERIAL PRIMARY KEY,
	asin VARCHAR(10) NOT NULL,
	binding VARCHAR(50) NOT NULL,
	format VARCHAR(50)  NOT NULL,
	num_discs INTEGER,
	CONSTRAINT valid_discs CHECK (num_discs > 0),
	releasedate DATE NOT NULL,
	upc VARCHAR(12) UNIQUE NOT NULL,
	CONSTRAINT music_upc_numeric CHECK (upc ~ '^[0-9]{12}$'), --ob NULL akzeptieren mit  OR NULL? es gibt ja schon ean
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM musicspec
--product_reviews
DROP TABLE IF EXISTS product_reviews CASCADE;
CREATE TABLE IF NOT EXISTS product_reviews (
    review_id SERIAL PRIMARY KEY,
    asin VARCHAR(10) NOT NULL,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    helpful INTEGER DEFAULT 0,
    reviewdate DATE NOT NULL,
    username VARCHAR(255) NOT NULL,
    summary TEXT,
    review_content TEXT,
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM product_reviews
--Kategorien
DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    parent_id INTEGER REFERENCES categories(id) ON DELETE CASCADE
);
--SELECT * FROM categories
--Kategorien zu item match
DROP TABLE IF EXISTS item_categories CASCADE;
CREATE TABLE item_categories (
    asin VARCHAR(10) NOT NULL,
    category_id INTEGER REFERENCES categories(id),
	PRIMARY KEY(asin, category_id),
	FOREIGN KEY (asin) REFERENCES item(asin) ON DELETE CASCADE
);
--SELECT * FROM item_categories










