--------------------------------LÖSCHE DOPPELTE ITEMS (gleiche asin) DUPLIKATE RAUS ABER SPEICHERE BEIDE PREISE----------------
-- Create a temporary table to store duplicates and the item ID to keep
DROP TABLE IF EXISTS duplicate_items;
CREATE TEMP TABLE duplicate_items AS
WITH DuplicateItems AS (
    SELECT 
        asin, -- replace this with the columns that define a duplicate item
        MIN(item_id) AS item_id_to_keep,
        ARRAY_AGG(item_id) AS duplicate_item_ids
    FROM 
        item
    GROUP BY 
        asin -- replace this with the columns that define a duplicate item
    HAVING 
        COUNT(*) > 1
)
SELECT
    asin, 
    item_id_to_keep,
    UNNEST(duplicate_item_ids) AS duplicate_item_id
FROM 
    DuplicateItems;

-- Step 2: Update the price table to link to the retained item IDs
-- Update the price table to link to the retained item IDs
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
-- Step 3: Delete duplicate items from the item table
DELETE FROM 
    item 
USING 
    duplicate_items di
WHERE 
    item.item_id = di.duplicate_item_id
AND 
    item.item_id <> di.item_id_to_keep;

---------------------------------------
--QUERY DIE ALLE PREIS DUPLIKATE RETURNT. ABER DA DIE DOPPELTEN PREISE IMMER VERSCHIEDENE SHOP_ID HABEN GIBT ES KEINE
SELECT item_id, price_state, price_mult, price_currency, price_value, price_shop_id, COUNT(*)
FROM price
GROUP BY item_id, price_state, price_mult, price_currency, price_value, price_shop_id
HAVING COUNT(*) > 1;