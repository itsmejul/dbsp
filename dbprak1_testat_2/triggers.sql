-- Aufgabe 2b: Konsistenz der avg_review_score wahren, wenn reviews erstellt, gelöscht oder geupdated werden

 
-- triggerfunktion, die die avg_review_score neu berechnet
CREATE OR REPLACE FUNCTION update_avg_review()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
	-- berechne avg_review_score neu und update die items mit dem neuen Wert
        UPDATE item
        SET avg_review_score = (
            SELECT AVG(CAST(rating AS float))
            FROM product_reviews
            WHERE asin = OLD.asin
        )
        WHERE asin = OLD.asin; -- bei delete muss man OLD nehmen, da die Zeile gelöscht wird und es somit keine neue Zeile gibt.
                               -- OLD ist also die zeile die gelöscht wird, und aus der ziehen wir die ASIN raus
    ELSE
        UPDATE item
        SET avg_review_score = (
            SELECT AVG(CAST(rating AS float))
            FROM product_reviews
            WHERE asin = NEW.asin
        )
        WHERE asin = NEW.asin;   -- bei update oder insert gibt es NEW keyword, was die neu eingefügte zeile zurückgibt
                                -- so können wir auf die neue asin zugreifen
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;



DROP TRIGGER IF EXISTS update_avg_review_on_insert_or_update ON product_reviews;
DROP TRIGGER IF EXISTS update_avg_review_on_delete ON product_reviews;


-- Trigger, der getriggert wird wenn neue reviews eingefügt werden oder geupdated werden
CREATE TRIGGER update_avg_review_on_insert_or_update
AFTER INSERT OR UPDATE ON product_reviews
FOR EACH ROW
EXECUTE FUNCTION update_avg_review();

-- trigger für das löschen von reihen
CREATE TRIGGER update_avg_review_on_delete
AFTER DELETE ON product_reviews
FOR EACH ROW
EXECUTE FUNCTION update_avg_review();





-- Trigger-Funktion, die ausgelöst wird, wenn Customer gelöscht wird. 
-- Sie ersetzt die customer_id durch die id vom deleted user
CREATE OR REPLACE FUNCTION update_reviews_to_deleted_user()
RETURNS TRIGGER AS $$
BEGIN
    -- Update customer_id auf deleted user
    UPDATE product_reviews
    SET customer_id = 0
    WHERE customer_id = OLD.customer_id;
    
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Trigger, der auslöst, bevor ein Customer gelöscht wird
CREATE TRIGGER before_delete_customer
BEFORE DELETE ON customer
FOR EACH ROW
EXECUTE FUNCTION update_reviews_to_deleted_user();

