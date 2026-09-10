-- SAEP - Tema 13: Estúdio de Tatuagem e Piercing
-- Primeiro execute 01_criar_banco.sql. Depois conecte-se a saep_agendamento_db.
-- Script destinado a um banco novo. Não apaga dados existentes.
BEGIN;
CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    documento VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(255) NOT NULL,
    email VARCHAR(255)
);
CREATE TABLE tatuadores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    especialidade VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE macas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);
CREATE TABLE agendamentos (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES clientes(id),
    tatuador_id BIGINT NOT NULL REFERENCES tatuadores(id),
    maca_id BIGINT NOT NULL REFERENCES macas(id),
    data DATE NOT NULL,
    hora TIME NOT NULL,
    servico VARCHAR(255) NOT NULL,
    observacao VARCHAR(255),
    CONSTRAINT uk_agendamento_tatuador_data_hora UNIQUE (tatuador_id, data, hora),
    CONSTRAINT uk_agendamento_maca_data_hora UNIQUE (maca_id, data, hora)
);
INSERT INTO tatuadores (nome, especialidade, ativo) VALUES
('Ana Silva', 'Fine line e minimalista', TRUE),
('Bruno Santos', 'Blackwork e pontilhismo', TRUE),
('Caio Oliveira', 'Realismo', TRUE),
('Duda Costa', 'Aquarela e colorida', TRUE),
('Enzo Ferreira', 'Old school e tradicional', TRUE);
INSERT INTO macas (nome) VALUES ('Maca 1'), ('Maca 2'), ('Maca 3'), ('Maca 4'), ('Maca 5');
COMMIT;
