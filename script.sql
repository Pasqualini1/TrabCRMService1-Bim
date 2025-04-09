CREATE TABLE medicos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    crm VARCHAR(20) NOT NULL,
    especialidade VARCHAR(50) NOT NULL,
    rua VARCHAR(100) NOT NULL,
    numero VARCHAR(20),
    complemento VARCHAR(50),
    bairro VARCHAR(50) NOT NULL,
    cidade VARCHAR(50) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE pacientes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    telefone VARCHAR(20),
    rua VARCHAR(100),
    numero VARCHAR(10),
    complemento VARCHAR(50),
    bairro VARCHAR(50),
    cidade VARCHAR(50),
    estado VARCHAR(2),
    cep VARCHAR(10),
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE consultas (
    id SERIAL PRIMARY KEY,
    nome_medico VARCHAR(100) NOT NULL,
    crm VARCHAR(20) NOT NULL,
    especialidade VARCHAR(50) NOT NULL,
    nome_paciente VARCHAR(100) NOT NULL,
    cpf_paciente VARCHAR(14) NOT NULL,
    data TIMESTAMP NOT NULL,
    ativa BOOLEAN NOT NULL DEFAULT TRUE,
    motivo_cancelamento TEXT
);

SELECT * FROM medicos;
SELECT * FROM pacientes;
SELECT * FROM consultas;
