ALTER TABLE tb_product
    ADD COLUMN is_in_stock BOOLEAN DEFAULT TRUE;

UPDATE tb_product
    SET is_in_stock = TRUE;

ALTER TABLE tb_product
    ALTER COLUMN is_in_stock SET NOT NULL;