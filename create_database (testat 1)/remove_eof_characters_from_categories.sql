UPDATE categories SET title = REPLACE(title, '\r', '');

UPDATE categories SET title = REPLACE(title, '\n', '');
UPDATE categories SET title = REPLACE(title, '\r\n', '');

-- Remove Carriage Return characters
UPDATE categories SET title = REPLACE(title, CHR(13), '');

-- Remove Line Feed characters
UPDATE categories SET title = REPLACE(title, CHR(10), '');