CREATE SCHEMA desafio;


CREATE TABLE desafio.transacao (
    id SERIAL NOT NULL,
    cartao VARCHAR(30),
    valor FLOAT,
    data_hora TIMESTAMP,
    estabelecimento VARCHAR(100),
    tipo_pagamento VARCHAR(30),
    status VARCHAR(25),
    nsu VARCHAR(50),
    codigo_autorizacao VARCHAR(30),
    parcelas INT,

    PRIMARY KEY (id)
);