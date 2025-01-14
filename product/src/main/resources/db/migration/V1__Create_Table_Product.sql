CREATE TABLE product (
    id SERIAL PRIMARY KEY,       -- Chave primária, gerada automaticamente
    name VARCHAR(255) NOT NULL UNIQUE,  -- Nome do produto, obrigatório
    price NUMERIC(10, 2) NOT NULL -- Preço do produto, obrigatório, com duas casas decimais
);
