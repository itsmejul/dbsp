DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    parent_id INTEGER REFERENCES categories(id)
);

DROP TABLE IF EXISTS item_categories CASCADE;
CREATE TABLE item_categories (
    asin TEXT NOT NULL,
    category_id INTEGER REFERENCES categories(id)
);


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
        INSERT INTO item_categories (asin, category_id)
        VALUES ((xpath('/item/text()', item))[1]::TEXT, cat_id);
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

SELECT * FROM price;