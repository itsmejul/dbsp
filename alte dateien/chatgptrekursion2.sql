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








CREATE TEMP TABLE xml_data (content XML);

INSERT INTO xml_data (content) VALUES ('
<categories>
    <category>Features
        <category>old features
            <item>1111</item>  
            <item>1112</item>  
            <category>really old features</category>  
        </category>
        <item>1112</item>  
    </category>
    <category>Singles
        <item>1113</item>  
        <category>new singles</category>  
    </category>
</categories>
');











------------------------------------------------------REKURSIVE VARIANTE GEHT NICHT ----------------------------------


-- Helper function to extract the first text node
CREATE OR REPLACE FUNCTION xpath_string(xml XML, xpath_expr TEXT)
RETURNS TEXT AS $$
DECLARE
    result TEXT;
BEGIN
    SELECT xpath(xpath_expr, xml)::TEXT INTO result;
    RETURN result;
END;
$$ LANGUAGE plpgsql;

-- Recursive function to process categories and items
CREATE OR REPLACE FUNCTION process_category(v_category XML, v_parent_id INT)
RETURNS VOID AS $$
DECLARE
    v_name TEXT;
    v_sub_category XML;
    v_item XML;
    v_category_id INT;
    v_item_id TEXT;
BEGIN
    -- Extract category name
    v_name := xpath_string(v_category, 'node()[1]');

    -- Insert category
    INSERT INTO categories (name, parent_id) VALUES (v_name, v_parent_id)
    RETURNING id INTO v_category_id;

    -- Process subcategories
    FOR v_sub_category IN 
        SELECT unnest(xpath('category', v_category))
    LOOP
        PERFORM process_category(v_sub_category, v_category_id);
    END LOOP;

    -- Process items
    FOR v_item IN 
        SELECT unnest(xpath('item', v_category))
    LOOP
        v_item_id := xpath_string(v_item, 'text()');
        
        -- Insert item if not exists
        INSERT INTO items (item_id) VALUES (v_item_id)
        ON CONFLICT (item_id) DO NOTHING;
        
        -- Insert item-category relationship
        INSERT INTO item_categories (item_id, category_id)
        SELECT id, v_category_id FROM items WHERE item_id = v_item_id;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

DO $$
DECLARE
    v_category XML;
BEGIN
    -- Process root categories
    FOR v_category IN 
        SELECT unnest(xpath('/categories/category', content)) FROM xml_data
    LOOP
        PERFORM process_category(v_category, NULL);
    END LOOP;
END
$$ LANGUAGE plpgsql;




------------------------------------ITERATIVE VARIANTE------------------------------------------------

-- Helper function to extract the first text node
CREATE OR REPLACE FUNCTION xpath_string(xml XML, xpath_expr TEXT)
RETURNS TEXT AS $$
DECLARE
    result TEXT;
BEGIN
    SELECT xpath(xpath_expr, xml)::TEXT INTO result;
    RETURN result;
END;
$$ LANGUAGE plpgsql;

-- Process the XML data iteratively
DO $$
DECLARE
    v_root XML;
    v_curr XML;
    v_category XML;
    v_item XML;
    v_name TEXT;
    v_parent_id INT;
    v_category_id INT;
    v_item_id TEXT;
    v_stack XML[] := ARRAY[]::XML[];
    v_stack_parent_id INT[] := ARRAY[]::INT[];
BEGIN
    -- Initialize the stack with the root categories
    FOR v_category IN 
        SELECT unnest(xpath('/categories/category', content)) FROM xml_data
    LOOP
        v_stack := array_append(v_stack, v_category);
        v_stack_parent_id := array_append(v_stack_parent_id, NULL);
    END LOOP;

    -- Iteratively process the stack
    WHILE array_length(v_stack, 1) > 0 LOOP
        v_curr := v_stack[array_length(v_stack, 1)];
        v_parent_id := v_stack_parent_id[array_length(v_stack_parent_id, 1)];
        v_stack := v_stack[:array_length(v_stack, 1) - 1];
        v_stack_parent_id := v_stack_parent_id[:array_length(v_stack_parent_id, 1) - 1];

        -- Extract category name
        v_name := xpath_string(v_curr, 'node()[1]');

        -- Insert category
        INSERT INTO categories (name, parent_id) VALUES (v_name, v_parent_id)
        RETURNING id INTO v_category_id;

        -- Process subcategories
        FOR v_category IN 
            SELECT unnest(xpath('category', v_curr))
        LOOP
            v_stack := array_append(v_stack, v_category);
            v_stack_parent_id := array_append(v_stack_parent_id, v_category_id);
        END LOOP;

        -- Process items
        FOR v_item IN 
            SELECT unnest(xpath('item', v_curr))
        LOOP
            v_item_id := xpath_string(v_item, 'text()');
            
            -- Insert item if not exists
            INSERT INTO items (item_id) VALUES (v_item_id)
            ON CONFLICT (item_id) DO NOTHING;
            
            -- Insert item-category relationship
            INSERT INTO item_categories (item_id, category_id)
            SELECT id, v_category_id FROM items WHERE item_id = v_item_id;
        END LOOP;
    END LOOP;
END
$$ LANGUAGE plpgsql;

