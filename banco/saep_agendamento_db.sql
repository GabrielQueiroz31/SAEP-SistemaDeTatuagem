-- SAEP - Tema 13: Estúdio de Tatuagem e Piercing
-- Execute este arquivo no PostgreSQL antes de iniciar a API.

-- Execute 01_criar_banco.sql uma vez. Depois conecte-se ao banco
-- saep_agendamento_db e execute este arquivo.

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

CREATE TABLE agendamentos (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES clientes(id),
    tatuador_id BIGINT NOT NULL REFERENCES tatuadores(id),
    data DATE NOT NULL,
    hora TIME NOT NULL,
    servico VARCHAR(255) NOT NULL,
    observacao VARCHAR(255),
    CONSTRAINT uk_agendamento_tatuador_data_hora UNIQUE (tatuador_id, data, hora)
);

INSERT INTO tatuadores (nome, especialidade, ativo) VALUES
('Ana Ink', 'Fine line e minimalista', TRUE),
('Bruno Black', 'Blackwork e pontilhismo', TRUE),
('Caio Art', 'Realismo', TRUE),
('Duda Color', 'Aquarela e colorida', TRUE),
('Enzo Old', 'Old school e tradicional', TRUE);
