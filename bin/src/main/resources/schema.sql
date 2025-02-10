CREATE TABLE card (
    card_id BIGINT PRIMARY KEY,
    card_holder_name  VARCHAR(255) NOT NULL,
    expiration_date VARCHAR(7) NOT NULL,
    active BOOLEAN DEFAULT false,
    balance DECIMAL(10, 2) DEFAULT 0.0,
    blocked BOOLEAN DEFAULT false,
    currency VARCHAR(3) DEFAULT 'USD'
);
