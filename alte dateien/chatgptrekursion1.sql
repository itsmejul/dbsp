
SET max_stack_depth = '3MB'; 
DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    parent_id INTEGER REFERENCES categories(id)
);

DROP TABLE IF EXISTS items CASCADE;
CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    item_id VARCHAR NOT NULL
);

DROP TABLE IF EXISTS item_categories CASCADE;
CREATE TABLE item_categories (
    item_id INTEGER REFERENCES items(id),
    category_id INTEGER REFERENCES categories(id),
    PRIMARY KEY (item_id, category_id)
);






DROP TABLE IF EXISTS xml_data;
CREATE TEMP TABLE xml_data (content XML);

--INSERT INTO xml_data (content) SELECT unnest(xpath('//categories',
--XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('C:\temp\categories.xml'), 'UTF8')::xml)));


INSERT INTO xml_data (content) VALUES ('
<categories>
    <category>test</category>
</categories>
');











DO $$
DECLARE
    v_category XML;
    v_item XML;
    v_name TEXT;
    v_parent_id INT;
    v_category_id INT;
    v_item_id INT;
BEGIN
    -- Process root categories
    FOR v_category IN 
        SELECT unnest(xpath('/categories/category', content)) FROM xml_data
    LOOP
        PERFORM process_category(v_category, NULL);
    END LOOP;
END
$$ LANGUAGE plpgsql;

-- Function to process categories recursively
CREATE OR REPLACE FUNCTION process_category(v_category XML, v_parent_id INT)
RETURNS VOID AS $$
DECLARE
    v_name TEXT;
    v_sub_category XML;
    v_item XML;
    v_category_id INT;
    v_item_id INT;
BEGIN
    v_name := 
unnest(xpath('//text()', v_category::xml))::text;
    -- Insert category
    INSERT INTO categories (name, parent_id) VALUES (v_name, v_parent_id)
    RETURNING id INTO v_category_id;

    -- Process subcategories
    FOR v_sub_category IN 
        SELECT unnest(xpath('//category', v_category))
    LOOP
        PERFORM process_category(v_sub_category, v_category_id);
    END LOOP;

END;
$$ LANGUAGE plpgsql;

-- Helper function to extract text from XML
CREATE OR REPLACE FUNCTION xpath_string(xml XML, xpath_expr TEXT)
RETURNS TEXT AS $$
DECLARE
    result TEXT;
BEGIN
    SELECT xpath(xpath_expr, xml)::TEXT INTO result;
    RETURN result;
END;
$$ LANGUAGE plpgsql;